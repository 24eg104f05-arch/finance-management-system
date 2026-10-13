package com.finance.app.controller;

import com.finance.app.dto.tax.TaxEstimateRequest;
import com.finance.app.dto.tax.TaxEstimateResponse;
import com.finance.app.entity.TaxRule;
import com.finance.app.service.TaxService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @PostMapping("/estimate")
    public ResponseEntity<TaxEstimateResponse> calculateTaxEstimate(@Valid @RequestBody TaxEstimateRequest req) {
        return ResponseEntity.ok(taxService.calculateTax(req));
    }

    @GetMapping("/rules")
    public ResponseEntity<List<TaxRule>> getTaxRules(@RequestParam(required = false) String financialYear) {
        return ResponseEntity.ok(taxService.getTaxRules(financialYear));
    }
}
