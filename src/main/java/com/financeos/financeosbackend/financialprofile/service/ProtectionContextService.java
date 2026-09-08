package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.ProtectionContextRequest;
import com.financeos.financeosbackend.financialprofile.dto.ProtectionContextResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.ProtectionContext;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import com.financeos.financeosbackend.financialprofile.repository.ProtectionContextRepository;
import org.springframework.stereotype.Service;

@Service
public class ProtectionContextService {

    private final CurrentUserService currentUserService;
    private final FinancialProfileRepository financialProfileRepository;
    private final ProtectionContextRepository contextRepository;

    public ProtectionContextService(
            CurrentUserService currentUserService,
            FinancialProfileRepository financialProfileRepository,
            ProtectionContextRepository contextRepository
    ) {
        this.currentUserService = currentUserService;
        this.financialProfileRepository = financialProfileRepository;
        this.contextRepository = contextRepository;
    }

    public ProtectionContextResponse save(
            ProtectionContextRequest request
    ) {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        ProtectionContext context =
                contextRepository.findByFinancialProfile(profile)
                        .orElseGet(ProtectionContext::new);

        context.setFinancialProfile(profile);
        context.setFamilyResponsibility(request.getFamilyResponsibility());
        context.setProtectionPriority(request.getProtectionPriority());
        context.setProtectionPreference(request.getProtectionPreference());

        return toResponse(contextRepository.save(context));
    }

    public ProtectionContextResponse get() {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        ProtectionContext context =
                contextRepository.findByFinancialProfile(profile)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Protection context not found"));

        return toResponse(context);
    }

    private ProtectionContextResponse toResponse(
            ProtectionContext context
    ) {
        return new ProtectionContextResponse(
                context.getId(),
                context.getFamilyResponsibility(),
                context.getProtectionPriority(),
                context.getProtectionPreference()
        );
    }
}