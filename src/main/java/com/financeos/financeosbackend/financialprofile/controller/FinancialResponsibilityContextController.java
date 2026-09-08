package com.financeos.financeosbackend.financialprofile.controller;

import com.financeos.financeosbackend.financialprofile.dto.FinancialResponsibilityContextRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialResponsibilityContextResponse;
import com.financeos.financeosbackend.financialprofile.service.FinancialResponsibilityContextService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles/responsibility")
public class FinancialResponsibilityContextController {

    private final FinancialResponsibilityContextService service;

    public FinancialResponsibilityContextController(
            FinancialResponsibilityContextService service
    ) {
        this.service = service;
    }

    @PutMapping
    public FinancialResponsibilityContextResponse save(
            @Valid @RequestBody FinancialResponsibilityContextRequest request
    ) {
        return service.save(request);
    }

    @GetMapping
    public FinancialResponsibilityContextResponse get() {
        return service.get();
    }
}