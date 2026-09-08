package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceAlreadyExistsException;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.financialprofile.dto.CreateFinancialProfileRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialProfileResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.mapper.FinancialProfileMapper;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;import com.financeos.financeosbackend.financialprofile.dto.PlanningHorizonRequest;
import com.financeos.financeosbackend.financialprofile.dto.PlanningHorizonResponse;import com.financeos.financeosbackend.financialprofile.dto.UpdateFinancialProfileRequest;

@Service
public class FinancialProfileService {

    private final FinancialProfileRepository financialProfileRepository;
    private final UserRepository userRepository;
    private final FinancialProfileMapper financialProfileMapper;
    private final CurrentUserService currentUserService;

    public FinancialProfileService(
            FinancialProfileRepository financialProfileRepository,
            UserRepository userRepository,
            FinancialProfileMapper financialProfileMapper,
            CurrentUserService currentUserService
    ) {
        this.financialProfileRepository = financialProfileRepository;
        this.userRepository = userRepository;
        this.financialProfileMapper = financialProfileMapper;
        this.currentUserService = currentUserService;
    }

    public FinancialProfileResponse createProfile(
            Long userId,
            CreateFinancialProfileRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        User currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You cannot access another user's financial profile"
            );
        }

        if (financialProfileRepository.findByUserId(userId).isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Financial profile already exists"
            );
        }

        FinancialProfile profile =
                financialProfileMapper.toEntity(request, user);

        FinancialProfile savedProfile =
                financialProfileRepository.save(profile);

        return financialProfileMapper.toResponse(savedProfile);
    }

    public FinancialProfileResponse getProfile(Long userId) {

        User currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You cannot access another user's financial profile"
            );
        }

        FinancialProfile profile =
                financialProfileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Financial profile not found"
                                )
                        );

        return financialProfileMapper.toResponse(profile);
    }

    public FinancialProfileResponse getMyProfile() {

        User currentUser = currentUserService.getCurrentUser();

        FinancialProfile profile =
                financialProfileRepository.findByUser(currentUser)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Financial profile not found"
                                )
                        );

        return financialProfileMapper.toResponse(profile);
    }


    public PlanningHorizonResponse updatePlanningHorizon(
            PlanningHorizonRequest request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        FinancialProfile profile =
                financialProfileRepository.findByUser(currentUser)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Financial profile not found"
                                )
                        );

        profile.setPlanningHorizon(request.getPlanningHorizon());

        FinancialProfile savedProfile =
                financialProfileRepository.save(profile);

        return new PlanningHorizonResponse(
                savedProfile.getPlanningHorizon()
        );
    }

    public FinancialProfileResponse updateMyProfile(
            UpdateFinancialProfileRequest request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        FinancialProfile profile =
                financialProfileRepository.findByUser(currentUser)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Financial profile not found"
                                )
                        );

        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setOccupation(request.getOccupation());
        profile.setEmploymentStatus(request.getEmploymentStatus());
        profile.setInvestmentExperience(request.getInvestmentExperience());
        profile.setPlanningHorizon(request.getPlanningHorizon());
        profile.setFinancialResponsibility(
                request.getFinancialResponsibility()
        );

        FinancialProfile savedProfile =
                financialProfileRepository.save(profile);

        return financialProfileMapper.toResponse(savedProfile);
    }
}