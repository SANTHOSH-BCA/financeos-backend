package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.IncomeNatureRequest;
import com.financeos.financeosbackend.financialprofile.dto.IncomeNatureResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.IncomeNature;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import com.financeos.financeosbackend.financialprofile.repository.IncomeNatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialProfileIncomeNatureService {

    private final CurrentUserService currentUserService;
    private final FinancialProfileRepository financialProfileRepository;
    private final IncomeNatureRepository incomeNatureRepository;

    public FinancialProfileIncomeNatureService(
            CurrentUserService currentUserService,
            FinancialProfileRepository financialProfileRepository,
            IncomeNatureRepository incomeNatureRepository
    ) {
        this.currentUserService = currentUserService;
        this.financialProfileRepository = financialProfileRepository;
        this.incomeNatureRepository = incomeNatureRepository;
    }

    public List<IncomeNatureResponse> saveIncomeNatures(
            List<IncomeNatureRequest> requests
    ) {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        incomeNatureRepository.deleteAll(
                incomeNatureRepository.findByFinancialProfile(profile)
        );

        List<IncomeNature> incomeNatures = requests.stream()
                .map(request -> new IncomeNature(
                        profile,
                        request.getIncomeNature()
                ))
                .toList();

        return incomeNatureRepository.saveAll(incomeNatures)
                .stream()
                .map(incomeNature -> new IncomeNatureResponse(
                        incomeNature.getId(),
                        incomeNature.getIncomeNature()
                ))
                .toList();
    }

    public List<IncomeNatureResponse> getIncomeNatures() {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        return incomeNatureRepository.findByFinancialProfile(profile)
                .stream()
                .map(incomeNature -> new IncomeNatureResponse(
                        incomeNature.getId(),
                        incomeNature.getIncomeNature()
                ))
                .toList();
    }
}