package com.finance.app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_products")
public class BankProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String bankName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BankProductType productType;

    @Column(nullable = false, length = 150)
    private String productName;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal interestRate; // Annual percentage e.g. 7.10%

    @Column(precision = 6, scale = 2)
    private BigDecimal seniorCitizenRate; // e.g. 7.60%

    @Column(nullable = false)
    private Integer minTenureMonths = 6;

    @Column(nullable = false)
    private Integer maxTenureMonths = 120;

    @Column(precision = 15, scale = 2)
    private BigDecimal minAmount = BigDecimal.ZERO;

    @Column(length = 500)
    private String features;

    @Column(nullable = false)
    private Boolean verifiedCurrent = true;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public BankProduct() {}

    public BankProduct(String bankName, BankProductType productType, String productName, BigDecimal interestRate,
                       BigDecimal seniorCitizenRate, Integer minTenureMonths, Integer maxTenureMonths,
                       BigDecimal minAmount, String features, Boolean verifiedCurrent) {
        this.bankName = bankName;
        this.productType = productType;
        this.productName = productName;
        this.interestRate = interestRate;
        this.seniorCitizenRate = seniorCitizenRate;
        this.minTenureMonths = minTenureMonths != null ? minTenureMonths : 6;
        this.maxTenureMonths = maxTenureMonths != null ? maxTenureMonths : 120;
        this.minAmount = minAmount != null ? minAmount : BigDecimal.ZERO;
        this.features = features;
        this.verifiedCurrent = verifiedCurrent != null ? verifiedCurrent : true;
        this.updatedAt = LocalDateTime.now();
    }

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
