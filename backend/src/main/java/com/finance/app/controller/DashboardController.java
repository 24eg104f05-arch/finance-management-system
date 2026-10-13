package com.finance.app.controller;

import com.finance.app.dto.dashboard.DashboardOverviewDto;
import com.finance.app.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDto> getOverview() {
        return ResponseEntity.ok(dashboardService.getDashboardOverview());
    }
}
