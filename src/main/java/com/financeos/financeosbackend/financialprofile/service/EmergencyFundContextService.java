package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.EmergencyFundContextRequest;
import com.financeos.financeosbackend.financialprofile.dto.EmergencyFundContextResponse;
import com.financeos.financeosbackend.financialprofile.entity.EmergencyFundContext;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.repository.EmergencyFundContextRepository;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class EmergencyFundContextService {

    private final CurrentUserService currentUserService;
    private final FinancialProfileRepository financialProfileRepository;
    private final EmergencyFundContextRepository contextRepository;

    public EmergencyFundContextService(
            CurrentUserService currentUserService,
            FinancialProfileRepository financialProfileRepository,
            EmergencyFundContextRepository contextRepository
    ) {
        this.currentUserService = currentUserService;
        this.financialProfileRepository = financialProfileRepository;
        this.contextRepository = contextRepository;
    }

    public EmergencyFundContextResponse save(
            EmergencyFundContextRequest request
    ) {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        EmergencyFundContext context =
                contextRepository.findByFinancialProfile(profile)
                        .orElseGet(EmergencyFundContext::new);

        context.setFinancialProfile(profile);
        context.setPreferredCoverage(request.getPreferredCoverage());
        context.setSafetyLevel(request.getSafetyLevel());

        return toResponse(contextRepository.save(context));
    }

    public EmergencyFundContextResponse get() {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        EmergencyFundContext context =
                contextRepository.findByFinancialProfile(profile)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Emergency fund context not found"));

        return toResponse(context);
    }

    private EmergencyFundContextResponse toResponse(
            EmergencyFundContext context
    ) {
        return new EmergencyFundContextResponse(
                context.getId(),
                context.getPreferredCoverage(),
                context.getSafetyLevel()
        );
    }
}