package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.LiabilityV2ApiResponse;
import com.financeos.financeosbackend.liability.service.LiabilityV2ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/liabilities")
public class LiabilityV2ApiController {

    private final LiabilityV2ApiService liabilityV2ApiService;

    public LiabilityV2ApiController(
            LiabilityV2ApiService liabilityV2ApiService
    ) {
        this.liabilityV2ApiService = liabilityV2ApiService;
    }

    @GetMapping("/overview")
    public ResponseEntity<LiabilityV2ApiResponse> getOverview() {

        return ResponseEntity.ok(
                liabilityV2ApiService.getOverview()
        );
    }
}