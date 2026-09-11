package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.CreateLiabilityRepaymentRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityRepaymentResponse;
import com.financeos.financeosbackend.liability.service.LiabilityRepaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/liabilities/{liabilityId}/repayments")
public class LiabilityRepaymentController {

    private final LiabilityRepaymentService repaymentService;

    public LiabilityRepaymentController(
            LiabilityRepaymentService repaymentService
    ) {
        this.repaymentService = repaymentService;
    }

    @PostMapping
    public ResponseEntity<LiabilityRepaymentResponse> createRepayment(
            @PathVariable Long liabilityId,
            @Valid @RequestBody CreateLiabilityRepaymentRequest request
    ) {
        LiabilityRepaymentResponse response =
                repaymentService.createRepayment(
                        liabilityId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<LiabilityRepaymentResponse>> getRepayments(
            @PathVariable Long liabilityId
    ) {
        return ResponseEntity.ok(
                repaymentService.getRepayments(liabilityId)
        );
    }

    @GetMapping("/{repaymentId}")
    public ResponseEntity<LiabilityRepaymentResponse> getRepayment(
            @PathVariable Long liabilityId,
            @PathVariable Long repaymentId
    ) {
        return ResponseEntity.ok(
                repaymentService.getRepayment(
                        liabilityId,
                        repaymentId
                )
        );
    }
}
