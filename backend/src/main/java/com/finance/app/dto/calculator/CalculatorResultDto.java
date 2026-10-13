package com.finance.app.dto.calculator;

import java.math.BigDecimal;
import java.util.List;

public class CalculatorResultDto {
    private String calculationType; // "SIMPLE_INTEREST", "COMPOUND_INTEREST", "FD", "LOAN_EMI"
    private BigDecimal principalAmount;
    private BigDecimal ratePercent;
    private BigDecimal timePeriodYears;
    private Integer tenureMonths;
    private BigDecimal interestEarnedOrPaid;
    private BigDecimal totalAmount;
    private BigDecimal monthlyEmi;
    private String formulaUsed;
    private String assumptions;
    private List<EmiScheduleItem> amortizationSchedule; // for EMI loans (first 12-24 items + summary)

    public CalculatorResultDto() {}

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(BigDecimal ratePercent) {
        this.ratePercent = ratePercent;
    }

    public BigDecimal getTimePeriodYears() {
        return timePeriodYears;
    }

    public void setTimePeriodYears(BigDecimal timePeriodYears) {
        this.timePeriodYears = timePeriodYears;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public BigDecimal getInterestEarnedOrPaid() {
        return interestEarnedOrPaid;
    }

    public void setInterestEarnedOrPaid(BigDecimal interestEarnedOrPaid) {
        this.interestEarnedOrPaid = interestEarnedOrPaid;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getMonthlyEmi() {
        return monthlyEmi;
    }

    public void setMonthlyEmi(BigDecimal monthlyEmi) {
        this.monthlyEmi = monthlyEmi;
    }

    public String getFormulaUsed() {
        return formulaUsed;
    }

    public void setFormulaUsed(String formulaUsed) {
        this.formulaUsed = formulaUsed;
    }

    public String getAssumptions() {
        return assumptions;
    }

    public void setAssumptions(String assumptions) {
        this.assumptions = assumptions;
    }

    public List<EmiScheduleItem> getAmortizationSchedule() {
        return amortizationSchedule;
    }

    public void setAmortizationSchedule(List<EmiScheduleItem> amortizationSchedule) {
        this.amortizationSchedule = amortizationSchedule;
    }
}
