package com.financeos.financeosbackend.financialprofile.controller;

import com.financeos.financeosbackend.financialprofile.dto.ProtectionContextRequest;
import com.financeos.financeosbackend.financialprofile.dto.ProtectionContextResponse;
import com.financeos.financeosbackend.financialprofile.service.ProtectionContextService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-profiles/protection")
public class ProtectionContextController {

    private final ProtectionContextService service;

    public ProtectionContextController(
            ProtectionContextService service
    ) {
        this.service = service;
    }

    @PutMapping
    public ProtectionContextResponse save(
            @Valid @RequestBody ProtectionContextRequest request
    ) {
        return service.save(request);
    }

    @GetMapping
    public ProtectionContextResponse get() {
        return service.get();
    }
}