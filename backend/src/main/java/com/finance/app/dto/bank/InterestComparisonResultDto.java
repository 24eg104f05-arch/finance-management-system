package com.finance.app.dto.bank;

import com.finance.app.entity.BankProduct;
import com.finance.app.entity.BankProductType;

import java.math.BigDecimal;

public class InterestComparisonResultDto {
    private Long id;
    private String bankName;
    private String productName;
    private BankProductType productType;
    private BigDecimal appliedRate;
    private BigDecimal principalAmount;
    private Integer tenureMonths;
    private BigDecimal estimatedInterest;
    private BigDecimal maturityAmount; // for savings/FD
    private BigDecimal monthlyEmi;      // for loans
    private BigDecimal totalRepayment;  // for loans
    private String features;
    private Boolean verifiedCurrent;

    public InterestComparisonResultDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BankProductType getProductType() {
        return productType;
    }

    public void setProductType(BankProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getAppliedRate() {
        return appliedRate;
    }

    public void setAppliedRate(BigDecimal appliedRate) {
        this.appliedRate = appliedRate;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public BigDecimal getEstimatedInterest() {
        return estimatedInterest;
    }

    public void setEstimatedInterest(BigDecimal estimatedInterest) {
        this.estimatedInterest = estimatedInterest;
    }

    public BigDecimal getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(BigDecimal maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public BigDecimal getMonthlyEmi() {
        return monthlyEmi;
    }

    public void setMonthlyEmi(BigDecimal monthlyEmi) {
        this.monthlyEmi = monthlyEmi;
    }

    public BigDecimal getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(BigDecimal totalRepayment) {
        this.totalRepayment = totalRepayment;
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
