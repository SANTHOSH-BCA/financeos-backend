package com.financeos.financeosbackend.financialprofile.controller;

import com.financeos.financeosbackend.financialprofile.dto.EmergencyFundContextRequest;
import com.financeos.financeosbackend.financialprofile.dto.EmergencyFundContextResponse;
import com.financeos.financeosbackend.financialprofile.service.EmergencyFundContextService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles/emergency-fund")
public class EmergencyFundContextController {

    private final EmergencyFundContextService service;

    public EmergencyFundContextController(
            EmergencyFundContextService service
    ) {
        this.service = service;
    }

    @PutMapping
    public EmergencyFundContextResponse save(
            @Valid @RequestBody EmergencyFundContextRequest request
    ) {
        return service.save(request);
    }

    @GetMapping
    public EmergencyFundContextResponse get() {
        return service.get();
    }
}