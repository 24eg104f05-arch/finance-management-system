package com.finance.app.dto.budget;

import java.math.BigDecimal;

public class BudgetStatusDto {
    private Long budgetId;
    private String category;
    private BigDecimal monthlyLimit;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Double percentageSpent;
    private Boolean exceeded;
    private String statusLevel; // "SAFE", "WARNING", "EXCEEDED"

    public BudgetStatusDto() {}

    public BudgetStatusDto(Long budgetId, String category, BigDecimal monthlyLimit, BigDecimal spentAmount, BigDecimal remainingAmount, Double percentageSpent, Boolean exceeded, String statusLevel) {
        this.budgetId = budgetId;
        this.category = category;
        this.monthlyLimit = monthlyLimit;
        this.spentAmount = spentAmount;
        this.remainingAmount = remainingAmount;
        this.percentageSpent = percentageSpent;
        this.exceeded = exceeded;
        this.statusLevel = statusLevel;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Double getPercentageSpent() {
        return percentageSpent;
    }

    public void setPercentageSpent(Double percentageSpent) {
        this.percentageSpent = percentageSpent;
    }

    public Boolean getExceeded() {
        return exceeded;
    }

    public void setExceeded(Boolean exceeded) {
        this.exceeded = exceeded;
    }

    public String getStatusLevel() {
        return statusLevel;
    }

    public void setStatusLevel(String statusLevel) {
        this.statusLevel = statusLevel;
    }
}
