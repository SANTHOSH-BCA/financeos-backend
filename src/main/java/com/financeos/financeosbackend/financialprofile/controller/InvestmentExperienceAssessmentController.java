package com.financeos.financeosbackend.financialprofile.controller;

import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentRequest;
import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentResponse;
import com.financeos.financeosbackend.financialprofile.service.InvestmentExperienceAssessmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles/investment-experience")
public class InvestmentExperienceAssessmentController {

    private final InvestmentExperienceAssessmentService service;

    public InvestmentExperienceAssessmentController(
            InvestmentExperienceAssessmentService service
    ) {
        this.service = service;
    }

    @PutMapping
    public InvestmentExperienceAssessmentResponse save(
            @Valid @RequestBody InvestmentExperienceAssessmentRequest request
    ) {
        return service.saveAssessment(request);
    }

    @GetMapping
    public InvestmentExperienceAssessmentResponse get() {
        return service.getAssessment();
    }
}