package com.finance.app.dto.budget;

import com.finance.app.entity.Budget;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BudgetResponse {
    private Long id;
    private String category;
    private BigDecimal monthlyLimit;
    private Integer month;
    private Integer year;
    private LocalDateTime createdAt;

    public BudgetResponse() {}

    public BudgetResponse(Budget b) {
        this.id = b.getId();
        this.category = b.getCategory();
        this.monthlyLimit = b.getMonthlyLimit();
        this.month = b.getMonth();
        this.year = b.getYear();
        this.createdAt = b.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
