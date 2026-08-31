package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.exception.ResourceAlreadyExistsException;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.financialprofile.dto.CreateFinancialProfileRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialProfileResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.mapper.FinancialProfileMapper;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FinancialProfileService {

    private final FinancialProfileRepository financialProfileRepository;
    private final UserRepository userRepository;
    private final FinancialProfileMapper financialProfileMapper;

    public FinancialProfileService(
            FinancialProfileRepository financialProfileRepository,
            UserRepository userRepository,
            FinancialProfileMapper financialProfileMapper
    ) {
        this.financialProfileRepository = financialProfileRepository;
        this.userRepository = userRepository;
        this.financialProfileMapper = financialProfileMapper;
    }

    public FinancialProfileResponse createProfile(
            Long userId,
            CreateFinancialProfileRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

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

        FinancialProfile profile =
                financialProfileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Financial profile not found"
                                )
                        );

        return financialProfileMapper.toResponse(profile);
    }
}