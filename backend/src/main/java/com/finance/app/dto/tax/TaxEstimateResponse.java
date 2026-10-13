package com.finance.app.dto.tax;

import com.finance.app.entity.TaxRegime;

import java.math.BigDecimal;
import java.util.List;

public class TaxEstimateResponse {
    private String financialYear;
    private TaxRegime regime;
    private BigDecimal grossTotalIncome;
    private BigDecimal standardDeduction;
    private BigDecimal totalDeductions;
    private BigDecimal netTaxableIncome;
    private BigDecimal taxBeforeRebate;
    private BigDecimal rebate87A;
    private BigDecimal taxAfterRebate;
    private BigDecimal healthAndEducationCess; // 4%
    private BigDecimal totalTaxPayable;
    private Double effectiveTaxRate;
    private List<TaxSlabBreakdown> slabBreakdowns;
    private String regimeComparisonNote;
    private BigDecimal alternateRegimeTaxPayable;
    private String recommendedRegime;
    private String explanation;
    private String disclaimer = "This calculation is an estimate for educational and planning purposes only. It is based on official Indian Income Tax rates and rules. Consult a certified Chartered Accountant or tax advisor for official tax filing.";

    public TaxEstimateResponse() {}

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

    public BigDecimal getGrossTotalIncome() {
        return grossTotalIncome;
    }

    public void setGrossTotalIncome(BigDecimal grossTotalIncome) {
        this.grossTotalIncome = grossTotalIncome;
    }

    public BigDecimal getStandardDeduction() {
        return standardDeduction;
    }

    public void setStandardDeduction(BigDecimal standardDeduction) {
        this.standardDeduction = standardDeduction;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(BigDecimal totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public BigDecimal getNetTaxableIncome() {
        return netTaxableIncome;
    }

    public void setNetTaxableIncome(BigDecimal netTaxableIncome) {
        this.netTaxableIncome = netTaxableIncome;
    }

    public BigDecimal getTaxBeforeRebate() {
        return taxBeforeRebate;
    }

    public void setTaxBeforeRebate(BigDecimal taxBeforeRebate) {
        this.taxBeforeRebate = taxBeforeRebate;
    }

    public BigDecimal getRebate87A() {
        return rebate87A;
    }

    public void setRebate87A(BigDecimal rebate87A) {
        this.rebate87A = rebate87A;
    }

    public BigDecimal getTaxAfterRebate() {
        return taxAfterRebate;
    }

    public void setTaxAfterRebate(BigDecimal taxAfterRebate) {
        this.taxAfterRebate = taxAfterRebate;
    }

    public BigDecimal getHealthAndEducationCess() {
        return healthAndEducationCess;
    }

    public void setHealthAndEducationCess(BigDecimal healthAndEducationCess) {
        this.healthAndEducationCess = healthAndEducationCess;
    }

    public BigDecimal getTotalTaxPayable() {
        return totalTaxPayable;
    }

    public void setTotalTaxPayable(BigDecimal totalTaxPayable) {
        this.totalTaxPayable = totalTaxPayable;
    }

    public Double getEffectiveTaxRate() {
        return effectiveTaxRate;
    }

    public void setEffectiveTaxRate(Double effectiveTaxRate) {
        this.effectiveTaxRate = effectiveTaxRate;
    }

    public List<TaxSlabBreakdown> getSlabBreakdowns() {
        return slabBreakdowns;
    }

    public void setSlabBreakdowns(List<TaxSlabBreakdown> slabBreakdowns) {
        this.slabBreakdowns = slabBreakdowns;
    }

    public String getRegimeComparisonNote() {
        return regimeComparisonNote;
    }

    public void setRegimeComparisonNote(String regimeComparisonNote) {
        this.regimeComparisonNote = regimeComparisonNote;
    }

    public BigDecimal getAlternateRegimeTaxPayable() {
        return alternateRegimeTaxPayable;
    }

    public void setAlternateRegimeTaxPayable(BigDecimal alternateRegimeTaxPayable) {
        this.alternateRegimeTaxPayable = alternateRegimeTaxPayable;
    }

    public String getRecommendedRegime() {
        return recommendedRegime;
    }

    public void setRecommendedRegime(String recommendedRegime) {
        this.recommendedRegime = recommendedRegime;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
