package com.finance.app.dto.transaction;

import java.math.BigDecimal;
import java.util.List;

public class MonthlySummaryDto {
    private Integer month;
    private Integer year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netSavings;
    private Double savingsRate;
    private List<CategoryExpenseDto> categoryBreakdown;

    public MonthlySummaryDto() {}

    public MonthlySummaryDto(Integer month, Integer year, BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal netSavings, Double savingsRate, List<CategoryExpenseDto> categoryBreakdown) {
        this.month = month;
        this.year = year;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netSavings = netSavings;
        this.savingsRate = savingsRate;
        this.categoryBreakdown = categoryBreakdown;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(BigDecimal netSavings) {
        this.netSavings = netSavings;
    }

    public Double getSavingsRate() {
        return savingsRate;
    }

    public void setSavingsRate(Double savingsRate) {
        this.savingsRate = savingsRate;
    }

    public List<CategoryExpenseDto> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategoryExpenseDto> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }
}
