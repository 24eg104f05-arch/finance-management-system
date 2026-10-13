package com.finance.app.dto.calculator;

import java.math.BigDecimal;

public class EmiScheduleItem {
    private Integer monthNumber;
    private BigDecimal beginningBalance;
    private BigDecimal emiAmount;
    private BigDecimal principalComponent;
    private BigDecimal interestComponent;
    private BigDecimal endingBalance;

    public EmiScheduleItem() {}

    public EmiScheduleItem(Integer monthNumber, BigDecimal beginningBalance, BigDecimal emiAmount, BigDecimal principalComponent, BigDecimal interestComponent, BigDecimal endingBalance) {
        this.monthNumber = monthNumber;
        this.beginningBalance = beginningBalance;
        this.emiAmount = emiAmount;
        this.principalComponent = principalComponent;
        this.interestComponent = interestComponent;
        this.endingBalance = endingBalance;
    }

    public Integer getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
    }

    public BigDecimal getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public BigDecimal getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(BigDecimal emiAmount) {
        this.emiAmount = emiAmount;
    }

    public BigDecimal getPrincipalComponent() {
        return principalComponent;
    }

    public void setPrincipalComponent(BigDecimal principalComponent) {
        this.principalComponent = principalComponent;
    }

    public BigDecimal getInterestComponent() {
        return interestComponent;
    }

    public void setInterestComponent(BigDecimal interestComponent) {
        this.interestComponent = interestComponent;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }
}
