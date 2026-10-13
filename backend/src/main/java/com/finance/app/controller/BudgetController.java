package com.finance.app.controller;

import com.finance.app.dto.budget.BudgetRequest;
import com.finance.app.dto.budget.BudgetResponse;
import com.finance.app.dto.budget.BudgetStatusDto;
import com.finance.app.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets() {
        return ResponseEntity.ok(budgetService.getUserBudgets());
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> saveBudget(@Valid @RequestBody BudgetRequest req) {
        return ResponseEntity.ok(budgetService.saveBudget(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public ResponseEntity<List<BudgetStatusDto>> getBudgetStatuses(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(budgetService.getBudgetStatuses(month, year));
    }
}
