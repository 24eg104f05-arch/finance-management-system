package com.finance.app.service;

import com.finance.app.dto.tax.TaxEstimateRequest;
import com.finance.app.dto.tax.TaxEstimateResponse;
import com.finance.app.dto.tax.TaxSlabBreakdown;
import com.finance.app.entity.TaxRegime;
import com.finance.app.entity.TaxRule;
import com.finance.app.repository.TaxRuleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaxService {

    private final TaxRuleRepository taxRuleRepository;

    public TaxService(TaxRuleRepository taxRuleRepository) {
        this.taxRuleRepository = taxRuleRepository;
    }

    public List<TaxRule> getTaxRules(String financialYear) {
        String fy = (financialYear != null && !financialYear.isBlank()) ? financialYear.trim() : "2024-2025";
        return taxRuleRepository.findByFinancialYearOrderByRegimeAscSlabOrderAsc(fy);
    }

    public TaxEstimateResponse calculateTax(TaxEstimateRequest req) {
        TaxEstimateResponse primaryResult = computeTaxForRegime(req, req.getRegime());

        // Also compute alternate regime for clear comparison
        TaxRegime alternateRegime = (req.getRegime() == TaxRegime.NEW) ? TaxRegime.OLD : TaxRegime.NEW;
        TaxEstimateResponse alternateResult = computeTaxForRegime(req, alternateRegime);

        primaryResult.setAlternateRegimeTaxPayable(alternateResult.getTotalTaxPayable());

        BigDecimal diff = primaryResult.getTotalTaxPayable().subtract(alternateResult.getTotalTaxPayable());
        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            primaryResult.setRecommendedRegime(primaryResult.getRegime().name());
            primaryResult.setRegimeComparisonNote(String.format("The selected %s Regime saves ₹%s in tax compared to the %s Regime.",
                    primaryResult.getRegime().name(), diff.abs().toPlainString(), alternateRegime.name()));
        } else if (diff.compareTo(BigDecimal.ZERO) > 0) {
            primaryResult.setRecommendedRegime(alternateRegime.name());
            primaryResult.setRegimeComparisonNote(String.format("Switching to %s Regime would save you ₹%s in taxes compared to the selected %s Regime.",
                    alternateRegime.name(), diff.abs().toPlainString(), primaryResult.getRegime().name()));
        } else {
            primaryResult.setRecommendedRegime("EITHER");
            primaryResult.setRegimeComparisonNote("Both regimes result in the identical tax liability.");
        }

        return primaryResult;
    }

    private TaxEstimateResponse computeTaxForRegime(TaxEstimateRequest req, TaxRegime regime) {
        BigDecimal gross = req.getGrossAnnualIncome().add(
                req.getIncomeFromOtherSources() != null ? req.getIncomeFromOtherSources() : BigDecimal.ZERO
        );

        BigDecimal stdDeduction;
        BigDecimal deductions = BigDecimal.ZERO;

        if (regime == TaxRegime.NEW) {
            // New Regime: Standard deduction ₹75,000 for FY 24-25/25-26
            stdDeduction = new BigDecimal("75000.00");
        } else {
            // Old Regime: Standard deduction ₹50,000 + 80C, 80D, 80CCD, HRA, home loan 24b
            stdDeduction = new BigDecimal("50000.00");

            BigDecimal c80 = req.getSection80C() != null ? req.getSection80C().min(new BigDecimal("150000.00")) : BigDecimal.ZERO;
            BigDecimal d80 = req.getSection80D() != null ? req.getSection80D().min(new BigDecimal("50000.00")) : BigDecimal.ZERO;
            BigDecimal ccd = req.getSection80CCD1B() != null ? req.getSection80CCD1B().min(new BigDecimal("50000.00")) : BigDecimal.ZERO;
            BigDecimal hra = req.getHraExemption() != null ? req.getHraExemption() : BigDecimal.ZERO;
            BigDecimal homeLoan = req.getHomeLoanInterest24b() != null ? req.getHomeLoanInterest24b().min(new BigDecimal("200000.00")) : BigDecimal.ZERO;
            BigDecimal others = req.getOtherDeductions() != null ? req.getOtherDeductions() : BigDecimal.ZERO;

            deductions = c80.add(d80).add(ccd).add(hra).add(homeLoan).add(others);
        }

        BigDecimal totalAllowedDeductions = stdDeduction.add(deductions);
        BigDecimal netTaxable = gross.subtract(totalAllowedDeductions).max(BigDecimal.ZERO);

        List<TaxRule> slabs = taxRuleRepository.findByFinancialYearAndRegimeOrderBySlabOrderAsc(req.getFinancialYear(), regime);
        if (slabs.isEmpty()) {
            // Fallback default slabs if DB rules not yet initialized
            slabs = getDefaultSlabs(req.getFinancialYear(), regime);
        }

        List<TaxSlabBreakdown> breakdowns = new ArrayList<>();
        BigDecimal totalSlabTax = BigDecimal.ZERO;

        for (TaxRule slab : slabs) {
            BigDecimal min = slab.getSlabMin();
            BigDecimal max = slab.getSlabMax();
            BigDecimal rate = slab.getRatePercent();

            if (netTaxable.compareTo(min) > 0) {
                BigDecimal upperLimit = (max != null) ? netTaxable.min(max) : netTaxable;
                BigDecimal taxableInSlab = upperLimit.subtract(min).max(BigDecimal.ZERO);
                BigDecimal taxForSlab = taxableInSlab.multiply(rate)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                totalSlabTax = totalSlabTax.add(taxForSlab);

                String label = (max != null) ?
                        String.format("₹%s - ₹%s (%s%%)", min.toPlainString(), max.toPlainString(), rate.toPlainString()) :
                        String.format("Above ₹%s (%s%%)", min.toPlainString(), rate.toPlainString());

                breakdowns.add(new TaxSlabBreakdown(label, rate, taxableInSlab, taxForSlab));
            } else {
                String label = (max != null) ?
                        String.format("₹%s - ₹%s (%s%%)", min.toPlainString(), max.toPlainString(), rate.toPlainString()) :
                        String.format("Above ₹%s (%s%%)", min.toPlainString(), rate.toPlainString());
                breakdowns.add(new TaxSlabBreakdown(label, rate, BigDecimal.ZERO, BigDecimal.ZERO));
            }
        }

        // Section 87A rebate calculation
        BigDecimal rebate = BigDecimal.ZERO;
        if (regime == TaxRegime.NEW) {
            // Full rebate if taxable income <= 7,00,000 (tax up to 25,000 waived)
            if (netTaxable.compareTo(new BigDecimal("700000.00")) <= 0) {
                rebate = totalSlabTax.min(new BigDecimal("25000.00"));
            }
        } else {
            // Old regime: full rebate if taxable income <= 5,00,000 (tax up to 12,500 waived)
            if (netTaxable.compareTo(new BigDecimal("500000.00")) <= 0) {
                rebate = totalSlabTax.min(new BigDecimal("12500.00"));
            }
        }

        BigDecimal taxAfterRebate = totalSlabTax.subtract(rebate).max(BigDecimal.ZERO);

        // Health & Education Cess: 4%
        BigDecimal cess = taxAfterRebate.multiply(new BigDecimal("0.04")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalTax = taxAfterRebate.add(cess).setScale(2, RoundingMode.HALF_UP);

        double effectiveRate = 0.0;
        if (gross.compareTo(BigDecimal.ZERO) > 0) {
            effectiveRate = totalTax.divide(gross, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
        }

        String explanation = String.format(
                "Calculated under %s Tax Regime for FY %s. Gross Income: ₹%s. Standard Deduction: ₹%s. Additional Deductions: ₹%s. Net Taxable Income: ₹%s. Base Tax: ₹%s. Section 87A Rebate: ₹%s. 4%% Health & Education Cess: ₹%s. Total Tax Payable: ₹%s (Effective Rate: %.2f%%).",
                regime.name(), req.getFinancialYear(), gross.toPlainString(), stdDeduction.toPlainString(),
                deductions.toPlainString(), netTaxable.toPlainString(), totalSlabTax.toPlainString(),
                rebate.toPlainString(), cess.toPlainString(), totalTax.toPlainString(), effectiveRate
        );

        TaxEstimateResponse resp = new TaxEstimateResponse();
        resp.setFinancialYear(req.getFinancialYear());
        resp.setRegime(regime);
        resp.setGrossTotalIncome(gross);
        resp.setStandardDeduction(stdDeduction);
        resp.setTotalDeductions(deductions);
        resp.setNetTaxableIncome(netTaxable);
        resp.setTaxBeforeRebate(totalSlabTax);
        resp.setRebate87A(rebate);
        resp.setTaxAfterRebate(taxAfterRebate);
        resp.setHealthAndEducationCess(cess);
        resp.setTotalTaxPayable(totalTax);
        resp.setEffectiveTaxRate(effectiveRate);
        resp.setSlabBreakdowns(breakdowns);
        resp.setExplanation(explanation);

        return resp;
    }

    private List<TaxRule> getDefaultSlabs(String fy, TaxRegime regime) {
        List<TaxRule> list = new ArrayList<>();
        if (regime == TaxRegime.NEW) {
            list.add(new TaxRule(fy, regime, new BigDecimal("0.00"), new BigDecimal("300000.00"), BigDecimal.ZERO, 1, "0 to 3 Lakh: Nil"));
            list.add(new TaxRule(fy, regime, new BigDecimal("300000.00"), new BigDecimal("700000.00"), new BigDecimal("5.00"), 2, "3 to 7 Lakh: 5%"));
            list.add(new TaxRule(fy, regime, new BigDecimal("700000.00"), new BigDecimal("1000000.00"), new BigDecimal("10.00"), 3, "7 to 10 Lakh: 10%"));
            list.add(new TaxRule(fy, regime, new BigDecimal("1000000.00"), new BigDecimal("1200000.00"), new BigDecimal("15.00"), 4, "10 to 12 Lakh: 15%"));
            list.add(new TaxRule(fy, regime, new BigDecimal("1200000.00"), new BigDecimal("1500000.00"), new BigDecimal("20.00"), 5, "12 to 15 Lakh: 20%"));
            list.add(new TaxRule(fy, regime, new BigDecimal("1500000.00"), null, new BigDecimal("30.00"), 6, "Above 15 Lakh: 30%"));
        } else {
            list.add(new TaxRule(fy, regime, new BigDecimal("0.00"), new BigDecimal("250000.00"), BigDecimal.ZERO, 1, "0 to 2.5 Lakh: Nil"));
            list.add(new TaxRule(fy, regime, new BigDecimal("250000.00"), new BigDecimal("500000.00"), new BigDecimal("5.00"), 2, "2.5 to 5 Lakh: 5%"));
            list.add(new TaxRule(fy, regime, new BigDecimal("500000.00"), new BigDecimal("1000000.00"), new BigDecimal("20.00"), 3, "5 to 10 Lakh: 20%"));
            list.add(new TaxRule(fy, regime, new BigDecimal("1000000.00"), null, new BigDecimal("30.00"), 4, "Above 10 Lakh: 30%"));
        }
        return list;
    }
}
