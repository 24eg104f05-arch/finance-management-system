package com.finance.app.dto.tax;

import java.math.BigDecimal;

public class TaxSlabBreakdown {
    private String slabRange;
    private BigDecimal ratePercent;
    private BigDecimal taxableAmountInSlab;
    private BigDecimal taxForSlab;

    public TaxSlabBreakdown() {}

    public TaxSlabBreakdown(String slabRange, BigDecimal ratePercent, BigDecimal taxableAmountInSlab, BigDecimal taxForSlab) {
        this.slabRange = slabRange;
        this.ratePercent = ratePercent;
        this.taxableAmountInSlab = taxableAmountInSlab;
        this.taxForSlab = taxForSlab;
    }

    public String getSlabRange() {
        return slabRange;
    }

    public void setSlabRange(String slabRange) {
        this.slabRange = slabRange;
    }

    public BigDecimal getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(BigDecimal ratePercent) {
        this.ratePercent = ratePercent;
    }

    public BigDecimal getTaxableAmountInSlab() {
        return taxableAmountInSlab;
    }

    public void setTaxableAmountInSlab(BigDecimal taxableAmountInSlab) {
        this.taxableAmountInSlab = taxableAmountInSlab;
    }

    public BigDecimal getTaxForSlab() {
        return taxForSlab;
    }

    public void setTaxForSlab(BigDecimal taxForSlab) {
        this.taxForSlab = taxForSlab;
    }
}
