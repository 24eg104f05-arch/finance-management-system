package com.finance.app.service;

import com.finance.app.dto.tax.TaxEstimateRequest;
import com.finance.app.dto.tax.TaxEstimateResponse;
import com.finance.app.entity.TaxRegime;
import com.finance.app.repository.TaxRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class TaxServiceTest {

    private TaxService taxService;
    private TaxRuleRepository taxRuleRepository;

    @BeforeEach
    void setUp() {
        taxRuleRepository = Mockito.mock(TaxRuleRepository.class);
        Mockito.when(taxRuleRepository.findByFinancialYearAndRegimeOrderBySlabOrderAsc(Mockito.anyString(), Mockito.any()))
                .thenReturn(Collections.emptyList());
        taxService = new TaxService(taxRuleRepository);
    }

    @Test
    void testNewTaxRegimeUnder7LakhsTaxableHasZeroTaxDueTo87ARebate() {
        // Income = 7,00,000. Under New Regime:
        // Std Deduction = 75,000 -> Taxable = 6,25,000
        // Slabs: 0-3L: 0, 3-6.25L (3.25L) @ 5% = 16,250
        // Because taxable income <= 7,00,000, Section 87A rebate covers up to 25,000.
        // Therefore tax after rebate is 0, Cess is 0, Total tax payable is 0.00!
        TaxEstimateRequest req = new TaxEstimateRequest();
        req.setFinancialYear("2024-2025");
        req.setRegime(TaxRegime.NEW);
        req.setGrossAnnualIncome(new BigDecimal("700000.00"));

        TaxEstimateResponse resp = taxService.calculateTax(req);

        assertEquals(new BigDecimal("0.00"), resp.getTotalTaxPayable());
        assertTrue(resp.getRebate87A().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testNewTaxRegimeAbove7LakhsCalculatesAccurately() {
        // Income = 12,00,000.
        // Std Deduction = 75,000 -> Taxable = 11,25,000
        // Slabs:
        // 0-3L: 0
        // 3-7L (4L) @ 5% = 20,000
        // 7-10L (3L) @ 10% = 30,000
        // 10-11.25L (1.25L) @ 15% = 18,750
        // Base tax = 68,750. No 87A rebate (taxable > 7L).
        // 4% Cess = 2,750.
        // Total Tax = 71,500.00
        TaxEstimateRequest req = new TaxEstimateRequest();
        req.setFinancialYear("2024-2025");
        req.setRegime(TaxRegime.NEW);
        req.setGrossAnnualIncome(new BigDecimal("1200000.00"));

        TaxEstimateResponse resp = taxService.calculateTax(req);

        assertEquals(new BigDecimal("68750.00"), resp.getTaxBeforeRebate());
        assertEquals(new BigDecimal("2750.00"), resp.getHealthAndEducationCess());
        assertEquals(new BigDecimal("71500.00"), resp.getTotalTaxPayable());
    }
}
