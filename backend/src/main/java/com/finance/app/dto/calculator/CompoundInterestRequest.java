package com.finance.app.dto.calculator;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CompoundInterestRequest {
    @NotNull(message = "Principal is required")
    @DecimalMin(value = "1.0", message = "Principal must be at least 1")
    private BigDecimal principal;

    @NotNull(message = "Annual rate is required")
    @DecimalMin(value = "0.01", message = "Rate must be positive")
    private BigDecimal rate;

    @NotNull(message = "Time in years is required")
    @DecimalMin(value = "0.1", message = "Time must be positive")
    private BigDecimal timeYears;

    @NotNull(message = "Compounding frequency per year is required")
    @Min(value = 1, message = "Compounding frequency must be at least 1 (1=Annual, 2=Semi-annual, 4=Quarterly, 12=Monthly)")
    private Integer compoundingFrequency = 4; // Default quarterly (standard for FDs in India)

    public CompoundInterestRequest() {}

    public CompoundInterestRequest(BigDecimal principal, BigDecimal rate, BigDecimal timeYears, Integer compoundingFrequency) {
        this.principal = principal;
        this.rate = rate;
        this.timeYears = timeYears;
        this.compoundingFrequency = compoundingFrequency != null ? compoundingFrequency : 4;
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

    public Integer getCompoundingFrequency() {
        return compoundingFrequency;
    }

    public void setCompoundingFrequency(Integer compoundingFrequency) {
        this.compoundingFrequency = compoundingFrequency;
    }
}
