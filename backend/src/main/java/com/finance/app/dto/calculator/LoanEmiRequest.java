package com.finance.app.dto.calculator;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class LoanEmiRequest {
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "1000.0", message = "Loan amount must be at least 1,000")
    private BigDecimal loanAmount;

    @NotNull(message = "Annual interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be positive")
    private BigDecimal annualRate;

    @NotNull(message = "Tenure in months is required")
    @Min(value = 1, message = "Tenure must be at least 1 month")
    private Integer tenureMonths;

    public LoanEmiRequest() {}

    public LoanEmiRequest(BigDecimal loanAmount, BigDecimal annualRate, Integer tenureMonths) {
        this.loanAmount = loanAmount;
        this.annualRate = annualRate;
        this.tenureMonths = tenureMonths;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
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
}
