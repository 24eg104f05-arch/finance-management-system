package com.finance.app.dto.bank;

import com.finance.app.entity.BankProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BankProductRequest {

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotNull(message = "Product type is required")
    private BankProductType productType;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be positive")
    private BigDecimal interestRate;

    private BigDecimal seniorCitizenRate;

    private Integer minTenureMonths = 6;
    private Integer maxTenureMonths = 120;
    private BigDecimal minAmount = BigDecimal.ZERO;
    private String features;
    private Boolean verifiedCurrent = true;

    public BankProductRequest() {}

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BankProductType getProductType() {
        return productType;
    }

    public void setProductType(BankProductType productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getSeniorCitizenRate() {
        return seniorCitizenRate;
    }

    public void setSeniorCitizenRate(BigDecimal seniorCitizenRate) {
        this.seniorCitizenRate = seniorCitizenRate;
    }

    public Integer getMinTenureMonths() {
        return minTenureMonths;
    }

    public void setMinTenureMonths(Integer minTenureMonths) {
        this.minTenureMonths = minTenureMonths;
    }

    public Integer getMaxTenureMonths() {
        return maxTenureMonths;
    }

    public void setMaxTenureMonths(Integer maxTenureMonths) {
        this.maxTenureMonths = maxTenureMonths;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Boolean getVerifiedCurrent() {
        return verifiedCurrent;
    }

    public void setVerifiedCurrent(Boolean verifiedCurrent) {
        this.verifiedCurrent = verifiedCurrent;
    }
}
