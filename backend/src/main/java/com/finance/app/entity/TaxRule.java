package com.finance.app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tax_rules")
public class TaxRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String financialYear; // e.g. "2024-2025", "2025-2026"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TaxRegime regime; // NEW or OLD

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal slabMin;

    @Column(precision = 15, scale = 2)
    private BigDecimal slabMax; // null if upper bound unlimited

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal ratePercent;

    @Column(nullable = false)
    private Integer slabOrder; // order of evaluation

    @Column(length = 200)
    private String description;

    public TaxRule() {}

    public TaxRule(String financialYear, TaxRegime regime, BigDecimal slabMin, BigDecimal slabMax, BigDecimal ratePercent, Integer slabOrder, String description) {
        this.financialYear = financialYear;
        this.regime = regime;
        this.slabMin = slabMin;
        this.slabMax = slabMax;
        this.ratePercent = ratePercent;
        this.slabOrder = slabOrder;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getSlabMin() {
        return slabMin;
    }

    public void setSlabMin(BigDecimal slabMin) {
        this.slabMin = slabMin;
    }

    public BigDecimal getSlabMax() {
        return slabMax;
    }

    public void setSlabMax(BigDecimal slabMax) {
        this.slabMax = slabMax;
    }

    public BigDecimal getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(BigDecimal ratePercent) {
        this.ratePercent = ratePercent;
    }

    public Integer getSlabOrder() {
        return slabOrder;
    }

    public void setSlabOrder(Integer slabOrder) {
        this.slabOrder = slabOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
