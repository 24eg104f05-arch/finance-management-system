package com.finance.app.repository;

import com.finance.app.entity.TaxRegime;
import com.finance.app.entity.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long> {
    List<TaxRule> findByFinancialYearAndRegimeOrderBySlabOrderAsc(String financialYear, TaxRegime regime);
    List<TaxRule> findByFinancialYearOrderByRegimeAscSlabOrderAsc(String financialYear);
    List<TaxRule> findAllByOrderByFinancialYearDescRegimeAscSlabOrderAsc();
    void deleteByFinancialYearAndRegime(String financialYear, TaxRegime regime);
}
