package com.finance.app.dto.dashboard;

import com.finance.app.dto.budget.BudgetStatusDto;
import com.finance.app.dto.transaction.CategoryExpenseDto;
import com.finance.app.dto.transaction.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;

public class DashboardOverviewDto {
    private BigDecimal totalIncomeAllTime;
    private BigDecimal totalExpenseAllTime;
    private BigDecimal totalSavingsAllTime;

    private BigDecimal currentMonthIncome;
    private BigDecimal currentMonthExpense;
    private BigDecimal currentMonthSavings;
    private Double currentMonthSavingsRate;

    private List<TransactionResponse> recentTransactions;
    private List<CategoryExpenseDto> categoryBreakdownCurrentMonth;
    private List<BudgetStatusDto> budgetStatuses;

    public DashboardOverviewDto() {}

    public BigDecimal getTotalIncomeAllTime() {
        return totalIncomeAllTime;
    }

    public void setTotalIncomeAllTime(BigDecimal totalIncomeAllTime) {
        this.totalIncomeAllTime = totalIncomeAllTime;
    }

    public BigDecimal getTotalExpenseAllTime() {
        return totalExpenseAllTime;
    }

    public void setTotalExpenseAllTime(BigDecimal totalExpenseAllTime) {
        this.totalExpenseAllTime = totalExpenseAllTime;
    }

    public BigDecimal getTotalSavingsAllTime() {
        return totalSavingsAllTime;
    }

    public void setTotalSavingsAllTime(BigDecimal totalSavingsAllTime) {
        this.totalSavingsAllTime = totalSavingsAllTime;
    }

    public BigDecimal getCurrentMonthIncome() {
        return currentMonthIncome;
    }

    public void setCurrentMonthIncome(BigDecimal currentMonthIncome) {
        this.currentMonthIncome = currentMonthIncome;
    }

    public BigDecimal getCurrentMonthExpense() {
        return currentMonthExpense;
    }

    public void setCurrentMonthExpense(BigDecimal currentMonthExpense) {
        this.currentMonthExpense = currentMonthExpense;
    }

    public BigDecimal getCurrentMonthSavings() {
        return currentMonthSavings;
    }

    public void setCurrentMonthSavings(BigDecimal currentMonthSavings) {
        this.currentMonthSavings = currentMonthSavings;
    }

    public Double getCurrentMonthSavingsRate() {
        return currentMonthSavingsRate;
    }

    public void setCurrentMonthSavingsRate(Double currentMonthSavingsRate) {
        this.currentMonthSavingsRate = currentMonthSavingsRate;
    }

    public List<TransactionResponse> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<TransactionResponse> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    public List<CategoryExpenseDto> getCategoryBreakdownCurrentMonth() {
        return categoryBreakdownCurrentMonth;
    }

    public void setCategoryBreakdownCurrentMonth(List<CategoryExpenseDto> categoryBreakdownCurrentMonth) {
        this.categoryBreakdownCurrentMonth = categoryBreakdownCurrentMonth;
    }

    public List<BudgetStatusDto> getBudgetStatuses() {
        return budgetStatuses;
    }

    public void setBudgetStatuses(List<BudgetStatusDto> budgetStatuses) {
        this.budgetStatuses = budgetStatuses;
    }
}
