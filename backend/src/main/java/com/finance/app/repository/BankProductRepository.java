package com.finance.app.repository;

import com.finance.app.entity.BankProduct;
import com.finance.app.entity.BankProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankProductRepository extends JpaRepository<BankProduct, Long> {
    List<BankProduct> findByProductTypeOrderByInterestRateDesc(BankProductType productType);
    List<BankProduct> findByProductTypeOrderByInterestRateAsc(BankProductType productType);
    List<BankProduct> findAllByOrderByBankNameAscProductNameAsc();
    List<BankProduct> findByBankNameContainingIgnoreCaseOrProductNameContainingIgnoreCase(String bankName, String productName);
}
