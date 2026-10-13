package com.finance.app.dto.bank;

import com.finance.app.entity.BankProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InterestComparisonRequest {

    @NotNull(message = "Product type is required")
    private BankProductType productType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "100.00", message = "Amount must be at least 100")
    private BigDecimal amount;

    @NotNull(message = "Tenure in months is required")
    private Integer tenureMonths;

    private Boolean isSeniorCitizen = false;

    public InterestComparisonRequest() {}

    public InterestComparisonRequest(BankProductType productType, BigDecimal amount, Integer tenureMonths, Boolean isSeniorCitizen) {
        this.productType = productType;
        this.amount = amount;
        this.tenureMonths = tenureMonths;
        this.isSeniorCitizen = isSeniorCitizen != null ? isSeniorCitizen : false;
    }

    public BankProductType getProductType() {
        return productType;
    }

    public void setProductType(BankProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public Boolean getIsSeniorCitizen() {
        return isSeniorCitizen;
    }

    public void setIsSeniorCitizen(Boolean seniorCitizen) {
        isSeniorCitizen = seniorCitizen;
    }
}
