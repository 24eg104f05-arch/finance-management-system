package com.finance.app.service;

import com.finance.app.dto.bank.BankProductRequest;
import com.finance.app.dto.bank.InterestComparisonRequest;
import com.finance.app.dto.bank.InterestComparisonResultDto;
import com.finance.app.entity.BankProduct;
import com.finance.app.entity.BankProductType;
import com.finance.app.repository.BankProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankProductService {

    private final BankProductRepository bankProductRepository;

    public BankProductService(BankProductRepository bankProductRepository) {
        this.bankProductRepository = bankProductRepository;
    }

    public List<BankProduct> getAllProducts() {
        return bankProductRepository.findAllByOrderByBankNameAscProductNameAsc();
    }

    public List<BankProduct> getProductsByType(BankProductType type) {
        if (type == BankProductType.SAVINGS || type == BankProductType.FD) {
            return bankProductRepository.findByProductTypeOrderByInterestRateDesc(type);
        } else {
            return bankProductRepository.findByProductTypeOrderByInterestRateAsc(type);
        }
    }

    public List<InterestComparisonResultDto> compareProducts(InterestComparisonRequest req) {
        List<BankProduct> products = getProductsByType(req.getProductType());
        List<InterestComparisonResultDto> results = new ArrayList<>();

        BigDecimal principal = req.getAmount();
        int months = req.getTenureMonths();
        double timeYears = months / 12.0;

        for (BankProduct product : products) {
            BigDecimal rate = product.getInterestRate();
            if (Boolean.TRUE.equals(req.getIsSeniorCitizen()) && product.getSeniorCitizenRate() != null) {
                rate = product.getSeniorCitizenRate();
            }

            InterestComparisonResultDto dto = new InterestComparisonResultDto();
            dto.setId(product.getId());
            dto.setBankName(product.getBankName());
            dto.setProductName(product.getProductName());
            dto.setProductType(product.getProductType());
            dto.setAppliedRate(rate);
            dto.setPrincipalAmount(principal);
            dto.setTenureMonths(months);
            dto.setFeatures(product.getFeatures());
            dto.setVerifiedCurrent(product.getVerifiedCurrent());

            double annualRateDec = rate.doubleValue() / 100.0;

            if (product.getProductType() == BankProductType.SAVINGS || product.getProductType() == BankProductType.FD) {
                // Quarterly compounding standard for FDs: A = P * (1 + r/4)^(4*t)
                int n = (product.getProductType() == BankProductType.FD) ? 4 : 12; // FD quarterly, savings monthly
                double amount = principal.doubleValue() * Math.pow(1.0 + (annualRateDec / n), n * timeYears);
                BigDecimal maturity = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
                BigDecimal interestEarned = maturity.subtract(principal);

                dto.setMaturityAmount(maturity);
                dto.setEstimatedInterest(interestEarned);
                dto.setMonthlyEmi(BigDecimal.ZERO);
                dto.setTotalRepayment(maturity);
            } else {
                // Loan EMI: EMI = [P x r x (1+r)^n] / [(1+r)^n - 1] where r is monthly rate
                double monthlyRate = annualRateDec / 12.0;
                double emiVal;
                if (monthlyRate == 0) {
                    emiVal = principal.doubleValue() / months;
                } else {
                    double factor = Math.pow(1.0 + monthlyRate, months);
                    emiVal = (principal.doubleValue() * monthlyRate * factor) / (factor - 1.0);
                }

                BigDecimal emi = BigDecimal.valueOf(emiVal).setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalRepay = emi.multiply(BigDecimal.valueOf(months)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalInterest = totalRepay.subtract(principal).max(BigDecimal.ZERO);

                dto.setMonthlyEmi(emi);
                dto.setEstimatedInterest(totalInterest);
                dto.setTotalRepayment(totalRepay);
                dto.setMaturityAmount(BigDecimal.ZERO);
            }

            results.add(dto);
        }

        return results;
    }

    @Transactional
    public BankProduct createProduct(BankProductRequest req) {
        BankProduct product = new BankProduct(
                req.getBankName().trim(),
                req.getProductType(),
                req.getProductName().trim(),
                req.getInterestRate(),
                req.getSeniorCitizenRate(),
                req.getMinTenureMonths(),
                req.getMaxTenureMonths(),
                req.getMinAmount(),
                req.getFeatures(),
                req.getVerifiedCurrent()
        );
        return bankProductRepository.save(product);
    }

    @Transactional
    public BankProduct updateProduct(Long id, BankProductRequest req) {
        BankProduct product = bankProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank product not found"));

        product.setBankName(req.getBankName().trim());
        product.setProductType(req.getProductType());
        product.setProductName(req.getProductName().trim());
        product.setInterestRate(req.getInterestRate());
        product.setSeniorCitizenRate(req.getSeniorCitizenRate());
        product.setMinTenureMonths(req.getMinTenureMonths());
        product.setMaxTenureMonths(req.getMaxTenureMonths());
        product.setMinAmount(req.getMinAmount());
        product.setFeatures(req.getFeatures());
        product.setVerifiedCurrent(req.getVerifiedCurrent());
        product.setUpdatedAt(LocalDateTime.now());

        return bankProductRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        bankProductRepository.deleteById(id);
    }
}
