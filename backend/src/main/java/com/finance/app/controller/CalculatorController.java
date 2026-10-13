package com.finance.app.controller;

import com.finance.app.dto.calculator.*;
import com.finance.app.service.CalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/simple-interest")
    public ResponseEntity<CalculatorResultDto> calculateSimpleInterest(@Valid @RequestBody SimpleInterestRequest req) {
        return ResponseEntity.ok(calculatorService.calculateSimpleInterest(req));
    }

    @PostMapping("/compound-interest")
    public ResponseEntity<CalculatorResultDto> calculateCompoundInterest(@Valid @RequestBody CompoundInterestRequest req) {
        return ResponseEntity.ok(calculatorService.calculateCompoundInterest(req));
    }

    @PostMapping("/fd")
    public ResponseEntity<CalculatorResultDto> calculateFd(@Valid @RequestBody FdRequest req) {
        return ResponseEntity.ok(calculatorService.calculateFd(req));
    }

    @PostMapping("/loan-emi")
    public ResponseEntity<CalculatorResultDto> calculateLoanEmi(@Valid @RequestBody LoanEmiRequest req) {
        return ResponseEntity.ok(calculatorService.calculateLoanEmi(req));
    }
}
