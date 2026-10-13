package com.finance.app.service;

import com.finance.app.dto.calculator.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    void testCalculateSimpleInterest() {
        // Principal = 10,000, Rate = 5%, Time = 2 years -> SI = 1,000, Total = 11,000
        SimpleInterestRequest req = new SimpleInterestRequest(
                new BigDecimal("10000.00"),
                new BigDecimal("5.00"),
                new BigDecimal("2.00")
        );

        CalculatorResultDto result = calculatorService.calculateSimpleInterest(req);

        assertEquals(new BigDecimal("1000.00"), result.getInterestEarnedOrPaid());
        assertEquals(new BigDecimal("11000.00"), result.getTotalAmount());
    }

    @Test
    void testCalculateCompoundInterest() {
        // Principal = 10,000, Rate = 10%, Time = 2 years, Compounded Annually (n=1)
        // A = 10000 * (1 + 0.10)^2 = 12,100, CI = 2,100
        CompoundInterestRequest req = new CompoundInterestRequest(
                new BigDecimal("10000.00"),
                new BigDecimal("10.00"),
                new BigDecimal("2.00"),
                1
        );

        CalculatorResultDto result = calculatorService.calculateCompoundInterest(req);

        assertEquals(new BigDecimal("2100.00"), result.getInterestEarnedOrPaid());
        assertEquals(new BigDecimal("12100.00"), result.getTotalAmount());
    }

    @Test
    void testCalculateLoanEmi() {
        // Loan = 1,00,000, Rate = 12% p.a. (1% monthly), Tenure = 12 months
        // EMI = 100000 * 0.01 * (1.01)^12 / ((1.01)^12 - 1) ≈ 8884.88
        LoanEmiRequest req = new LoanEmiRequest(
                new BigDecimal("100000.00"),
                new BigDecimal("12.00"),
                12
        );

        CalculatorResultDto result = calculatorService.calculateLoanEmi(req);

        assertNotNull(result.getMonthlyEmi());
        assertTrue(result.getMonthlyEmi().compareTo(new BigDecimal("8800.00")) > 0);
        assertTrue(result.getMonthlyEmi().compareTo(new BigDecimal("8900.00")) < 0);
        assertEquals(12, result.getAmortizationSchedule().size());
    }
}
