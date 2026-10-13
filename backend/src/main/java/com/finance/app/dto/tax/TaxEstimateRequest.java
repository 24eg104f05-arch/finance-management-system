package com.finance.app.dto.tax;

import com.finance.app.entity.TaxRegime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TaxEstimateRequest {

    @NotBlank(message = "Financial year is required")
    private String financialYear = "2024-2025";

    @NotNull(message = "Tax regime is required")
    private TaxRegime regime = TaxRegime.NEW;

    @NotNull(message = "Gross annual income is required")
    @DecimalMin(value = "0.0", message = "Income cannot be negative")
    private BigDecimal grossAnnualIncome;

    private BigDecimal incomeFromOtherSources = BigDecimal.ZERO;

    // Deductions (primarily applied in Old Tax Regime)
    private BigDecimal section80C = BigDecimal.ZERO;         // Max 1,50,000
    private BigDecimal section80D = BigDecimal.ZERO;         // Health insurance
    private BigDecimal section80CCD1B = BigDecimal.ZERO;     // NPS extra 50,000
    private BigDecimal hraExemption = BigDecimal.ZERO;       // HRA
    private BigDecimal homeLoanInterest24b = BigDecimal.ZERO;// Max 2,00,000
    private BigDecimal otherDeductions = BigDecimal.ZERO;    // 80E, 80G, etc.

    private String ageCategory = "GENERAL"; // "GENERAL", "SENIOR", "SUPER_SENIOR"

    public TaxEstimateRequest() {}

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public TaxRegime getRegime() {
        return regime;
    }

    public void setRegime(TaxRegime regime) {
        this.regime = regime;
    }

    public BigDecimal getGrossAnnualIncome() {
        return grossAnnualIncome;
    }

    public void setGrossAnnualIncome(BigDecimal grossAnnualIncome) {
        this.grossAnnualIncome = grossAnnualIncome;
    }

    public BigDecimal getIncomeFromOtherSources() {
        return incomeFromOtherSources;
    }

    public void setIncomeFromOtherSources(BigDecimal incomeFromOtherSources) {
        this.incomeFromOtherSources = incomeFromOtherSources;
    }

    public BigDecimal getSection80C() {
        return section80C;
    }

    public void setSection80C(BigDecimal section80C) {
        this.section80C = section80C;
    }

    public BigDecimal getSection80D() {
        return section80D;
    }

    public void setSection80D(BigDecimal section80D) {
        this.section80D = section80D;
    }

    public BigDecimal getSection80CCD1B() {
        return section80CCD1B;
    }

    public void setSection80CCD1B(BigDecimal section80CCD1B) {
        this.section80CCD1B = section80CCD1B;
    }

    public BigDecimal getHraExemption() {
        return hraExemption;
    }

    public void setHraExemption(BigDecimal hraExemption) {
        this.hraExemption = hraExemption;
    }

    public BigDecimal getHomeLoanInterest24b() {
        return homeLoanInterest24b;
    }

    public void setHomeLoanInterest24b(BigDecimal homeLoanInterest24b) {
        this.homeLoanInterest24b = homeLoanInterest24b;
    }

    public BigDecimal getOtherDeductions() {
        return otherDeductions;
    }

    public void setOtherDeductions(BigDecimal otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }
}
