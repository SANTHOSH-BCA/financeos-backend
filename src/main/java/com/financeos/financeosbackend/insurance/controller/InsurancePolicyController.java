package com.financeos.financeosbackend.insurance.controller;

import com.financeos.financeosbackend.insurance.dto.request.CreateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.request.UpdateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.response.InsurancePolicyResponse;
import com.financeos.financeosbackend.insurance.service.InsurancePolicyService;
import com.financeos.financeosbackend.insurance.dto.request.RenewInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.response.InsuranceRenewalResponse;
import com.financeos.financeosbackend.insurance.dto.response.ProtectionAnalysisItem;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/insurance")
public class InsurancePolicyController {

    private final InsurancePolicyService insurancePolicyService;

    public InsurancePolicyController(
            InsurancePolicyService insurancePolicyService
    ) {
        this.insurancePolicyService = insurancePolicyService;
    }

    @PostMapping
    public ResponseEntity<InsurancePolicyResponse> createPolicy(
            @Valid @RequestBody CreateInsuranceRequest request
    ) {
        InsurancePolicyResponse response =
                insurancePolicyService.createPolicy(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<InsurancePolicyResponse>> getMyPolicies() {
        return ResponseEntity.ok(
                insurancePolicyService.getMyPolicies()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyResponse> getMyPolicy(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                insurancePolicyService.getMyPolicy(id)
        );
    }

    @GetMapping("/premiums/overdue")
    public ResponseEntity<List<InsurancePolicyResponse>> getOverduePremiums() {
        return ResponseEntity.ok(
                insurancePolicyService.getOverduePremiums()
        );
    }

    @GetMapping("/premiums/upcoming")
    public ResponseEntity<List<InsurancePolicyResponse>> getUpcomingPremiums() {
        return ResponseEntity.ok(
                insurancePolicyService.getUpcomingPremiums()
        );
    }

    @GetMapping("/expired")
    public ResponseEntity<List<InsurancePolicyResponse>> getExpiredPolicies() {
        return ResponseEntity.ok(
                insurancePolicyService.getExpiredPolicies()
        );
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<InsurancePolicyResponse>> getPoliciesExpiringSoon() {
        return ResponseEntity.ok(
                insurancePolicyService.getPoliciesExpiringSoon()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyResponse> updatePolicy(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInsuranceRequest request
    ) {
        return ResponseEntity.ok(
                insurancePolicyService.updatePolicy(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(
            @PathVariable Long id
    ) {
        insurancePolicyService.deletePolicy(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<InsuranceRenewalResponse> renewPolicy(
            @PathVariable Long id,
            @Valid @RequestBody RenewInsuranceRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(insurancePolicyService.renewPolicy(id, request));
    }

    @GetMapping("/{id}/renewals")
    public ResponseEntity<List<InsuranceRenewalResponse>> getRenewalHistory(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                insurancePolicyService.getRenewalHistory(id)
        );
    }

    @GetMapping("/protection-analysis")
    public ResponseEntity<List<ProtectionAnalysisItem>> getProtectionAnalysis() {
        return ResponseEntity.ok(
                insurancePolicyService.getProtectionAnalysis()
        );
    }
}
