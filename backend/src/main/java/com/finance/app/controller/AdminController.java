package com.finance.app.controller;

import com.finance.app.dto.bank.BankProductRequest;
import com.finance.app.entity.BankProduct;
import com.finance.app.entity.TaxRule;
import com.finance.app.repository.BankProductRepository;
import com.finance.app.repository.TaxRuleRepository;
import com.finance.app.repository.UserRepository;
import com.finance.app.service.BankProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final BankProductService bankProductService;
    private final BankProductRepository bankProductRepository;
    private final TaxRuleRepository taxRuleRepository;
    private final UserRepository userRepository;

    public AdminController(BankProductService bankProductService,
                           BankProductRepository bankProductRepository,
                           TaxRuleRepository taxRuleRepository,
                           UserRepository userRepository) {
        this.bankProductService = bankProductService;
        this.bankProductRepository = bankProductRepository;
        this.taxRuleRepository = taxRuleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        return ResponseEntity.ok(Map.of(
                "totalUsers", userRepository.count(),
                "totalBankProducts", bankProductRepository.count(),
                "totalTaxRules", taxRuleRepository.count(),
                "status", "OPERATIONAL"
        ));
    }

    // Bank Products Management
    @PostMapping("/banks")
    public ResponseEntity<BankProduct> createBankProduct(@Valid @RequestBody BankProductRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bankProductService.createProduct(req));
    }

    @PutMapping("/banks/{id}")
    public ResponseEntity<BankProduct> updateBankProduct(@PathVariable Long id, @Valid @RequestBody BankProductRequest req) {
        return ResponseEntity.ok(bankProductService.updateProduct(id, req));
    }

    @DeleteMapping("/banks/{id}")
    public ResponseEntity<Void> deleteBankProduct(@PathVariable Long id) {
        bankProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Tax Rules Management
    @PostMapping("/tax-rules")
    public ResponseEntity<TaxRule> createTaxRule(@Valid @RequestBody TaxRule rule) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taxRuleRepository.save(rule));
    }

    @DeleteMapping("/tax-rules/{id}")
    public ResponseEntity<Void> deleteTaxRule(@PathVariable Long id) {
        taxRuleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
