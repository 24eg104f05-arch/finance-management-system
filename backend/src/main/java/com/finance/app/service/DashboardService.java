package com.finance.app.service;

import com.finance.app.dto.dashboard.DashboardOverviewDto;
import com.finance.app.dto.transaction.MonthlySummaryDto;
import com.finance.app.dto.transaction.TransactionResponse;
import com.finance.app.entity.TransactionType;
import com.finance.app.entity.User;
import com.finance.app.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private final AuthService authService;

    public DashboardService(TransactionRepository transactionRepository,
                            TransactionService transactionService,
                            BudgetService budgetService,
                            AuthService authService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
        this.budgetService = budgetService;
        this.authService = authService;
    }

    public DashboardOverviewDto getDashboardOverview() {
        User user = authService.getAuthenticatedUser();
        LocalDate now = LocalDate.now();

        BigDecimal allTimeIncome = transactionRepository.sumAmountByUserIdAndType(user.getId(), TransactionType.INCOME);
        BigDecimal allTimeExpense = transactionRepository.sumAmountByUserIdAndType(user.getId(), TransactionType.EXPENSE);
        BigDecimal allTimeSavings = allTimeIncome.subtract(allTimeExpense);

        MonthlySummaryDto currentMonthSummary = transactionService.getMonthlySummary(now.getMonthValue(), now.getYear());

        List<TransactionResponse> recent = transactionRepository.findTop10ByUserIdOrderByTransactionDateDescCreatedAtDesc(user.getId())
                .stream().map(TransactionResponse::new).collect(Collectors.toList());

        DashboardOverviewDto dto = new DashboardOverviewDto();
        dto.setTotalIncomeAllTime(allTimeIncome);
        dto.setTotalExpenseAllTime(allTimeExpense);
        dto.setTotalSavingsAllTime(allTimeSavings);

        dto.setCurrentMonthIncome(currentMonthSummary.getTotalIncome());
        dto.setCurrentMonthExpense(currentMonthSummary.getTotalExpense());
        dto.setCurrentMonthSavings(currentMonthSummary.getNetSavings());
        dto.setCurrentMonthSavingsRate(currentMonthSummary.getSavingsRate());

        dto.setRecentTransactions(recent);
        dto.setCategoryBreakdownCurrentMonth(currentMonthSummary.getCategoryBreakdown());
        dto.setBudgetStatuses(budgetService.getBudgetStatuses(now.getMonthValue(), now.getYear()));

        return dto;
    }
}
