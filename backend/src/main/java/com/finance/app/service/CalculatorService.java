package com.finance.app.service;

import com.finance.app.dto.calculator.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculatorService {

    public CalculatorResultDto calculateSimpleInterest(SimpleInterestRequest req) {
        BigDecimal p = req.getPrincipal();
        BigDecimal r = req.getRate();
        BigDecimal t = req.getTimeYears();

        // SI = (P * R * T) / 100
        BigDecimal interest = p.multiply(r).multiply(t)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal total = p.add(interest);

        CalculatorResultDto res = new CalculatorResultDto();
        res.setCalculationType("SIMPLE_INTEREST");
        res.setPrincipalAmount(p);
        res.setRatePercent(r);
        res.setTimePeriodYears(t);
        res.setInterestEarnedOrPaid(interest);
        res.setTotalAmount(total);
        res.setFormulaUsed("Simple Interest: SI = (P × R × T) / 100; Total Amount: A = P + SI");
        res.setAssumptions("Interest is calculated on initial principal with no compounding. Linear growth over time.");

        return res;
    }

    public CalculatorResultDto calculateCompoundInterest(CompoundInterestRequest req) {
        double p = req.getPrincipal().doubleValue();
        double r = req.getRate().doubleValue() / 100.0;
        double t = req.getTimeYears().doubleValue();
        int n = req.getCompoundingFrequency();

        // A = P * (1 + r/n)^(n*t)
        double a = p * Math.pow(1.0 + (r / n), n * t);
        BigDecimal total = BigDecimal.valueOf(a).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interest = total.subtract(req.getPrincipal());

        String freqDesc = switch (n) {
            case 1 -> "Annually";
            case 2 -> "Semi-Annually";
            case 4 -> "Quarterly";
            case 12 -> "Monthly";
            case 365 -> "Daily";
            default -> n + " times/year";
        };

        CalculatorResultDto res = new CalculatorResultDto();
        res.setCalculationType("COMPOUND_INTEREST");
        res.setPrincipalAmount(req.getPrincipal());
        res.setRatePercent(req.getRate());
        res.setTimePeriodYears(req.getTimeYears());
        res.setInterestEarnedOrPaid(interest);
        res.setTotalAmount(total);
        res.setFormulaUsed("Compound Interest: A = P × (1 + r/n)^(n×t); Interest: CI = A - P");
        res.setAssumptions(String.format("Compounded %s (n=%d). Reinvested interest earns additional interest in subsequent compounding periods.", freqDesc, n));

        return res;
    }

    public CalculatorResultDto calculateFd(FdRequest req) {
        BigDecimal p = req.getDepositAmount();
        BigDecimal r = req.getAnnualRate();
        int months = req.getTenureMonths();
        double t = months / 12.0;
        int n = req.getCompoundingFrequency() != null ? req.getCompoundingFrequency() : 4; // default quarterly

        double pVal = p.doubleValue();
        double rVal = r.doubleValue() / 100.0;
        double aVal = pVal * Math.pow(1.0 + (rVal / n), n * t);

        BigDecimal maturity = BigDecimal.valueOf(aVal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interest = maturity.subtract(p);

        CalculatorResultDto res = new CalculatorResultDto();
        res.setCalculationType("FD");
        res.setPrincipalAmount(p);
        res.setRatePercent(r);
        res.setTenureMonths(months);
        res.setTimePeriodYears(BigDecimal.valueOf(t).setScale(2, RoundingMode.HALF_UP));
        res.setInterestEarnedOrPaid(interest);
        res.setTotalAmount(maturity);
        res.setFormulaUsed("FD Maturity: A = P × (1 + r/4)^(4×t) with quarterly compounding standard in Indian banks.");
        res.setAssumptions(String.format("Standard bank fixed deposit with quarterly compounding. Tenure is %d months. TDS and taxes may apply at maturity based on individual tax bracket.", months));

        return res;
    }

    public CalculatorResultDto calculateLoanEmi(LoanEmiRequest req) {
        BigDecimal p = req.getLoanAmount();
        BigDecimal annualRate = req.getAnnualRate();
        int months = req.getTenureMonths();

        double pVal = p.doubleValue();
        double monthlyRate = (annualRate.doubleValue() / 100.0) / 12.0;

        double emiVal;
        if (monthlyRate == 0) {
            emiVal = pVal / months;
        } else {
            double factor = Math.pow(1.0 + monthlyRate, months);
            emiVal = (pVal * monthlyRate * factor) / (factor - 1.0);
        }

        BigDecimal emi = BigDecimal.valueOf(emiVal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalPayment = emi.multiply(BigDecimal.valueOf(months)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalInterest = totalPayment.subtract(p);

        // Generate Amortization Schedule
        List<EmiScheduleItem> schedule = new ArrayList<>();
        double currentBalance = pVal;

        int scheduleLimit = Math.min(months, 36); // return up to 36 months schedule
        for (int m = 1; m <= scheduleLimit; m++) {
            double interestForMonth = currentBalance * monthlyRate;
            double principalForMonth = emiVal - interestForMonth;
            if (principalForMonth > currentBalance) {
                principalForMonth = currentBalance;
            }
            double endingBalance = Math.max(0.0, currentBalance - principalForMonth);

            schedule.add(new EmiScheduleItem(
                    m,
                    BigDecimal.valueOf(currentBalance).setScale(2, RoundingMode.HALF_UP),
                    emi,
                    BigDecimal.valueOf(principalForMonth).setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.valueOf(interestForMonth).setScale(2, RoundingMode.HALF_UP),
                    BigDecimal.valueOf(endingBalance).setScale(2, RoundingMode.HALF_UP)
            ));

            currentBalance = endingBalance;
        }

        CalculatorResultDto res = new CalculatorResultDto();
        res.setCalculationType("LOAN_EMI");
        res.setPrincipalAmount(p);
        res.setRatePercent(annualRate);
        res.setTenureMonths(months);
        res.setTimePeriodYears(BigDecimal.valueOf(months / 12.0).setScale(2, RoundingMode.HALF_UP));
        res.setMonthlyEmi(emi);
        res.setInterestEarnedOrPaid(totalInterest);
        res.setTotalAmount(totalPayment);
        res.setFormulaUsed("EMI = [P × r × (1+r)^n] / [(1+r)^n - 1] where r = annual rate / 1200 and n = tenure in months");
        res.setAssumptions(String.format("Fixed interest rate of %.2f%% p.a. Reducing balance method. Amortization schedule demonstrates principal and interest split.", annualRate));
        res.setAmortizationSchedule(schedule);

        return res;
    }
}
