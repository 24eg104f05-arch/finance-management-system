package com.finance.app.dto.calculator;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FdRequest {
    @NotNull(message = "Deposit amount is required")
    @DecimalMin(value = "100.0", message = "Deposit amount must be at least 100")
    private BigDecimal depositAmount;

    @NotNull(message = "Annual rate is required")
    @DecimalMin(value = "0.01", message = "Rate must be positive")
    private BigDecimal annualRate;

    @NotNull(message = "Tenure in months is required")
    @Min(value = 1, message = "Tenure must be at least 1 month")
    private Integer tenureMonths;

    // Compounding frequency: 1=Annual, 2=Half-yearly, 4=Quarterly, 12=Monthly. Default: 4 (Quarterly)
    private Integer compoundingFrequency = 4;

    public FdRequest() {}

    public FdRequest(BigDecimal depositAmount, BigDecimal annualRate, Integer tenureMonths, Integer compoundingFrequency) {
        this.depositAmount = depositAmount;
        this.annualRate = annualRate;
        this.tenureMonths = tenureMonths;
        this.compoundingFrequency = compoundingFrequency != null ? compoundingFrequency : 4;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public Integer getCompoundingFrequency() {
        return compoundingFrequency;
    }

    public void setCompoundingFrequency(Integer compoundingFrequency) {
        this.compoundingFrequency = compoundingFrequency;
    }
}
