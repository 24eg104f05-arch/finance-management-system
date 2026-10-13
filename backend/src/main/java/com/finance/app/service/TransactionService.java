package com.finance.app.service;

import com.finance.app.dto.transaction.CategoryExpenseDto;
import com.finance.app.dto.transaction.MonthlySummaryDto;
import com.finance.app.dto.transaction.TransactionRequest;
import com.finance.app.dto.transaction.TransactionResponse;
import com.finance.app.entity.Transaction;
import com.finance.app.entity.TransactionType;
import com.finance.app.entity.User;
import com.finance.app.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuthService authService;

    public TransactionService(TransactionRepository transactionRepository, AuthService authService) {
        this.transactionRepository = transactionRepository;
        this.authService = authService;
    }

    public List<TransactionResponse> getTransactions(String category, TransactionType type, LocalDate startDate, LocalDate endDate, String query) {
        User user = authService.getAuthenticatedUser();
        List<Transaction> list = transactionRepository.searchTransactions(
                user.getId(),
                (category != null && !category.isBlank()) ? category.trim() : null,
                type,
                startDate,
                endDate,
                (query != null && !query.isBlank()) ? query.trim() : null
        );

        return list.stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(Long id) {
        User user = authService.getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found or unauthorized"));
        return new TransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest req) {
        User user = authService.getAuthenticatedUser();
        Transaction transaction = new Transaction(
                user,
                req.getTitle().trim(),
                req.getAmount(),
                req.getType(),
                req.getCategory().trim(),
                req.getTransactionDate(),
                req.getDescription() != null ? req.getDescription().trim() : null
        );

        Transaction saved = transactionRepository.save(transaction);
        return new TransactionResponse(saved);
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionRequest req) {
        User user = authService.getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found or unauthorized"));

        transaction.setTitle(req.getTitle().trim());
        transaction.setAmount(req.getAmount());
        transaction.setType(req.getType());
        transaction.setCategory(req.getCategory().trim());
        transaction.setTransactionDate(req.getTransactionDate());
        transaction.setDescription(req.getDescription() != null ? req.getDescription().trim() : null);

        Transaction updated = transactionRepository.save(transaction);
        return new TransactionResponse(updated);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        User user = authService.getAuthenticatedUser();
        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found or unauthorized"));
        transactionRepository.delete(transaction);
    }

    public MonthlySummaryDto getMonthlySummary(Integer month, Integer year) {
        User user = authService.getAuthenticatedUser();
        LocalDate now = LocalDate.now();
        int m = (month != null) ? month : now.getMonthValue();
        int y = (year != null) ? year : now.getYear();

        YearMonth ym = YearMonth.of(y, m);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        BigDecimal income = transactionRepository.sumAmountByUserIdAndTypeAndDateBetween(user.getId(), TransactionType.INCOME, start, end);
        BigDecimal expense = transactionRepository.sumAmountByUserIdAndTypeAndDateBetween(user.getId(), TransactionType.EXPENSE, start, end);
        BigDecimal savings = income.subtract(expense);

        Double savingsRate = 0.0;
        if (income.compareTo(BigDecimal.ZERO) > 0) {
            savingsRate = savings.divide(income, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
        }

        List<Object[]> categorySums = transactionRepository.sumExpenseByCategoryForPeriod(user.getId(), start, end);
        List<CategoryExpenseDto> breakdowns = new ArrayList<>();

        for (Object[] row : categorySums) {
            String cat = (String) row[0];
            BigDecimal catAmount = (BigDecimal) row[1];
            double pct = 0.0;
            if (expense.compareTo(BigDecimal.ZERO) > 0) {
                pct = catAmount.divide(expense, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
            }
            breakdowns.add(new CategoryExpenseDto(cat, catAmount, pct));
        }

        return new MonthlySummaryDto(m, y, income, expense, savings, savingsRate, breakdowns);
    }
}
