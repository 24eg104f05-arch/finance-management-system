package com.finance.app.dto.calculator;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SimpleInterestRequest {
    @NotNull(message = "Principal is required")
    @DecimalMin(value = "1.0", message = "Principal must be at least 1")
    private BigDecimal principal;

    @NotNull(message = "Annual rate is required")
    @DecimalMin(value = "0.01", message = "Rate must be positive")
    private BigDecimal rate;

    @NotNull(message = "Time in years is required")
    @DecimalMin(value = "0.1", message = "Time must be positive")
    private BigDecimal timeYears;

    public SimpleInterestRequest() {}

    public SimpleInterestRequest(BigDecimal principal, BigDecimal rate, BigDecimal timeYears) {
        this.principal = principal;
        this.rate = rate;
        this.timeYears = timeYears;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getTimeYears() {
        return timeYears;
    }

    public void setTimeYears(BigDecimal timeYears) {
        this.timeYears = timeYears;
    }
}
