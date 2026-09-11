package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.mapper.LiabilityMapper;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.liability.validator.LiabilityValidator;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiabilityService {

    private final LiabilityRepository liabilityRepository;
    private final CurrentUserService currentUserService;
    private final LiabilityMapper liabilityMapper;
    private final LiabilityValidator liabilityValidator;

    public LiabilityService(
            LiabilityRepository liabilityRepository,
            CurrentUserService currentUserService,
            LiabilityMapper liabilityMapper,
            LiabilityValidator liabilityValidator
    ) {
        this.liabilityRepository = liabilityRepository;
        this.currentUserService = currentUserService;
        this.liabilityMapper = liabilityMapper;
        this.liabilityValidator = liabilityValidator;
    }

    public LiabilityResponse createLiability(
            CreateLiabilityRequest request
    ) {

        liabilityValidator.validateResponsibility(request);

        User user = currentUserService.getCurrentUser();

        Liability liability = liabilityMapper.toEntity(request);
        liability.setUser(user);

        Liability savedLiability =
                liabilityRepository.save(liability);

        return liabilityMapper.toResponse(savedLiability);
    }

    public List<LiabilityResponse> getMyLiabilities() {

        User user = currentUserService.getCurrentUser();

        return liabilityRepository.findAllByUser(user)
                .stream()
                .map(liabilityMapper::toResponse)
                .toList();
    }

    public LiabilityResponse getMyLiability(Long id) {

        User user = currentUserService.getCurrentUser();

        Liability liability =
                liabilityRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Liability not found"
                                )
                        );

        return liabilityMapper.toResponse(liability);
    }
}