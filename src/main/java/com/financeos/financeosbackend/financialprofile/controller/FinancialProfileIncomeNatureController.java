package com.financeos.financeosbackend.financialprofile.controller;

import com.financeos.financeosbackend.financialprofile.dto.IncomeNatureRequest;
import com.financeos.financeosbackend.financialprofile.dto.IncomeNatureResponse;
import com.financeos.financeosbackend.financialprofile.service.FinancialProfileIncomeNatureService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financial-profiles/income-natures")
public class FinancialProfileIncomeNatureController {

    private final FinancialProfileIncomeNatureService service;

    public FinancialProfileIncomeNatureController(
            FinancialProfileIncomeNatureService service
    ) {
        this.service = service;
    }

    @PutMapping
    public List<IncomeNatureResponse> save(
            @Valid @RequestBody List<@Valid IncomeNatureRequest> requests
    ) {
        return service.saveIncomeNatures(requests);
    }

    @GetMapping
    public List<IncomeNatureResponse> get() {
        return service.getIncomeNatures();
    }
}