package com.finance.app.service;

import com.finance.app.dto.budget.BudgetRequest;
import com.finance.app.dto.budget.BudgetResponse;
import com.finance.app.dto.budget.BudgetStatusDto;
import com.finance.app.entity.Budget;
import com.finance.app.entity.User;
import com.finance.app.repository.BudgetRepository;
import com.finance.app.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final AuthService authService;

    public BudgetService(BudgetRepository budgetRepository,
                         TransactionRepository transactionRepository,
                         AuthService authService) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.authService = authService;
    }

    public List<BudgetResponse> getUserBudgets() {
        User user = authService.getAuthenticatedUser();
        return budgetRepository.findByUserIdOrderByYearDescMonthDesc(user.getId())
                .stream().map(BudgetResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public BudgetResponse saveBudget(BudgetRequest req) {
        User user = authService.getAuthenticatedUser();
        String cat = req.getCategory().trim();

        Optional<Budget> existing = budgetRepository.findByUserIdAndCategoryIgnoreCaseAndMonthAndYear(
                user.getId(), cat, req.getMonth(), req.getYear());

        Budget budget;
        if (existing.isPresent()) {
            budget = existing.get();
            budget.setMonthlyLimit(req.getMonthlyLimit());
        } else {
            budget = new Budget(user, cat, req.getMonthlyLimit(), req.getMonth(), req.getYear());
        }

        Budget saved = budgetRepository.save(budget);
        return new BudgetResponse(saved);
    }

    @Transactional
    public void deleteBudget(Long id) {
        User user = authService.getAuthenticatedUser();
        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Budget not found or unauthorized"));
        budgetRepository.delete(budget);
    }

    public List<BudgetStatusDto> getBudgetStatuses(Integer month, Integer year) {
        User user = authService.getAuthenticatedUser();
        LocalDate now = LocalDate.now();
        int m = (month != null) ? month : now.getMonthValue();
        int y = (year != null) ? year : now.getYear();

        YearMonth ym = YearMonth.of(y, m);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<Budget> budgets = budgetRepository.findByUserIdAndMonthAndYear(user.getId(), m, y);
        List<BudgetStatusDto> statuses = new ArrayList<>();

        for (Budget b : budgets) {
            BigDecimal spent = transactionRepository.sumExpenseByCategoryForPeriod(user.getId(), b.getCategory(), start, end);
            if (spent == null) spent = BigDecimal.ZERO;

            BigDecimal limit = b.getMonthlyLimit();
            BigDecimal remaining = limit.subtract(spent);

            double percentageSpent = 0.0;
            if (limit.compareTo(BigDecimal.ZERO) > 0) {
                percentageSpent = spent.divide(limit, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
            }

            boolean exceeded = spent.compareTo(limit) > 0;
            String level = "SAFE";
            if (exceeded) {
                level = "EXCEEDED";
            } else if (percentageSpent >= 80.0) {
                level = "WARNING";
            }

            statuses.add(new BudgetStatusDto(
                    b.getId(),
                    b.getCategory(),
                    limit,
                    spent,
                    remaining,
                    percentageSpent,
                    exceeded,
                    level
            ));
        }

        return statuses;
    }
}
