package com.financeos.financeosbackend.financialprofile.controller;

import com.financeos.financeosbackend.financialprofile.dto.FinancialPriorityRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialPriorityResponse;
import com.financeos.financeosbackend.financialprofile.service.FinancialProfilePriorityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financial-profiles/priorities")
public class FinancialProfilePriorityController {

    private final FinancialProfilePriorityService service;

    public FinancialProfilePriorityController(
            FinancialProfilePriorityService service
    ) {
        this.service = service;
    }

    @PutMapping
    public List<FinancialPriorityResponse> save(
            @Valid @RequestBody List<@Valid FinancialPriorityRequest> requests
    ) {
        return service.savePriorities(requests);
    }

    @GetMapping
    public List<FinancialPriorityResponse> get() {
        return service.getPriorities();
    }
}