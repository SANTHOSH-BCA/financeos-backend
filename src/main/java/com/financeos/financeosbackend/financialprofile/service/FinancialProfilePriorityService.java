package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.FinancialPriorityRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialPriorityResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialPriority;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.repository.FinancialPriorityRepository;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialProfilePriorityService {

    private final CurrentUserService currentUserService;
    private final FinancialProfileRepository financialProfileRepository;
    private final FinancialPriorityRepository priorityRepository;

    public FinancialProfilePriorityService(
            CurrentUserService currentUserService,
            FinancialProfileRepository financialProfileRepository,
            FinancialPriorityRepository priorityRepository
    ) {
        this.currentUserService = currentUserService;
        this.financialProfileRepository = financialProfileRepository;
        this.priorityRepository = priorityRepository;
    }

    public List<FinancialPriorityResponse> savePriorities(
            List<FinancialPriorityRequest> requests
    ) {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        priorityRepository.deleteAll(
                priorityRepository
                        .findByFinancialProfileOrderByPriorityRankAsc(profile)
        );

        List<FinancialPriority> priorities = requests.stream()
                .map(request -> new FinancialPriority(
                        profile,
                        request.getPriority(),
                        request.getPriorityRank()
                ))
                .toList();

        return priorityRepository.saveAll(priorities)
                .stream()
                .map(priority -> new FinancialPriorityResponse(
                        priority.getId(),
                        priority.getPriority(),
                        priority.getPriorityRank()
                ))
                .sorted((a, b) ->
                        Integer.compare(
                                a.getPriorityRank(),
                                b.getPriorityRank()
                        ))
                .toList();
    }

    public List<FinancialPriorityResponse> getPriorities() {
        FinancialProfile profile =
                financialProfileRepository.findByUser(
                        currentUserService.getCurrentUser()
                ).orElseThrow(() ->
                        new RuntimeException("Financial profile not found"));

        return priorityRepository
                .findByFinancialProfileOrderByPriorityRankAsc(profile)
                .stream()
                .map(priority -> new FinancialPriorityResponse(
                        priority.getId(),
                        priority.getPriority(),
                        priority.getPriorityRank()
                ))
                .toList();
    }
}