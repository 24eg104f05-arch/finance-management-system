package com.finance.app.controller;

import com.finance.app.dto.bank.InterestComparisonRequest;
import com.finance.app.dto.bank.InterestComparisonResultDto;
import com.finance.app.entity.BankProduct;
import com.finance.app.entity.BankProductType;
import com.finance.app.service.BankProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banks")
public class BankProductController {

    private final BankProductService bankProductService;

    public BankProductController(BankProductService bankProductService) {
        this.bankProductService = bankProductService;
    }

    @GetMapping
    public ResponseEntity<List<BankProduct>> getAllProducts() {
        return ResponseEntity.ok(bankProductService.getAllProducts());
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<BankProduct>> getProductsByType(@RequestParam BankProductType type) {
        return ResponseEntity.ok(bankProductService.getProductsByType(type));
    }

    @PostMapping("/compare")
    public ResponseEntity<List<InterestComparisonResultDto>> compareProducts(@Valid @RequestBody InterestComparisonRequest req) {
        return ResponseEntity.ok(bankProductService.compareProducts(req));
    }
}
