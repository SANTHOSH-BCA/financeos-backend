package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.FinancialResponsibilityContextRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialResponsibilityContextResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.FinancialResponsibilityContext;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import com.financeos.financeosbackend.financialprofile.repository.FinancialResponsibilityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class FinancialResponsibilityContextService {

    private final CurrentUserService currentUserService;
    private final FinancialProfileRepository financialProfileRepository;
    private final FinancialResponsibilityContextRepository contextRepository;

    public FinancialResponsibilityContextService(
            CurrentUserService currentUserService,
            FinancialProfileRepository financialProfileRepository,
            FinancialResponsibilityContextRepository contextRepository
    ) {
        this.currentUserService = currentUserService;
        this.financialProfileRepository = financialProfileRepository;
        this.contextRepository = contextRepository;
    }

    public FinancialResponsibilityContextResponse save(
            FinancialResponsibilityContextRequest request
    ) {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        FinancialResponsibilityContext context =
                contextRepository.findByFinancialProfile(profile)
                        .orElseGet(FinancialResponsibilityContext::new);

        context.setFinancialProfile(profile);
        context.setResponsibilityLevel(request.getResponsibilityLevel());
        context.setDependentsCount(request.getDependentsCount());
        context.setDependentContext(request.getDependentContext());

        return toResponse(contextRepository.save(context));
    }

    public FinancialResponsibilityContextResponse get() {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        FinancialResponsibilityContext context =
                contextRepository.findByFinancialProfile(profile)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Responsibility context not found"));

        return toResponse(context);
    }

    private FinancialResponsibilityContextResponse toResponse(
            FinancialResponsibilityContext context
    ) {
        return new FinancialResponsibilityContextResponse(
                context.getId(),
                context.getResponsibilityLevel(),
                context.getDependentsCount(),
                context.getDependentContext()
        );
    }
}