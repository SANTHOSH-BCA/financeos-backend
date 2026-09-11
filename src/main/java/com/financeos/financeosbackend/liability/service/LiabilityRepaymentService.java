package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.liability.dto.CreateLiabilityRepaymentRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityRepaymentResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.enums.LiabilityStatus;
import com.financeos.financeosbackend.liability.mapper.LiabilityRepaymentMapper;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.liability.validator.LiabilityRepaymentValidator;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LiabilityRepaymentService {

    private final LiabilityRepository liabilityRepository;
    private final LiabilityRepaymentRepository repaymentRepository;
    private final CurrentUserService currentUserService;
    private final LiabilityRepaymentMapper repaymentMapper;
    private final LiabilityRepaymentValidator repaymentValidator;

    public LiabilityRepaymentService(
            LiabilityRepository liabilityRepository,
            LiabilityRepaymentRepository repaymentRepository,
            CurrentUserService currentUserService,
            LiabilityRepaymentMapper repaymentMapper,
            LiabilityRepaymentValidator repaymentValidator
    ) {
        this.liabilityRepository = liabilityRepository;
        this.repaymentRepository = repaymentRepository;
        this.currentUserService = currentUserService;
        this.repaymentMapper = repaymentMapper;
        this.repaymentValidator = repaymentValidator;
    }

    @Transactional
    public LiabilityRepaymentResponse createRepayment(
            Long liabilityId,
            CreateLiabilityRepaymentRequest request
    ) {
        User user = currentUserService.getCurrentUser();

        Liability liability = liabilityRepository
                .findByIdAndUser(liabilityId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Liability not found"));

        repaymentValidator.validate(
                request,
                liability.getOutstandingAmount()
        );

        BigDecimal principalAmount = request.getPrincipalAmount();

        LiabilityRepayment repayment =
                repaymentMapper.toEntity(request, liability);

        LiabilityRepayment savedRepayment =
                repaymentRepository.save(repayment);

        if (principalAmount != null) {
            BigDecimal newOutstanding =
                    liability.getOutstandingAmount()
                            .subtract(principalAmount);

            if (newOutstanding.compareTo(BigDecimal.ZERO) <= 0) {
                liability.setOutstandingAmount(BigDecimal.ZERO);
                liability.setStatus(LiabilityStatus.PAID);
            } else {
                liability.setOutstandingAmount(newOutstanding);
            }

            liabilityRepository.save(liability);
        }

        return repaymentMapper.toResponse(savedRepayment);
    }

    @Transactional(readOnly = true)
    public List<LiabilityRepaymentResponse> getRepayments(
            Long liabilityId
    ) {
        User user = currentUserService.getCurrentUser();

        Liability liability = liabilityRepository
                .findByIdAndUser(liabilityId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Liability not found"));

        return repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability)
                .stream()
                .map(repaymentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LiabilityRepaymentResponse getRepayment(
            Long liabilityId,
            Long repaymentId
    ) {
        User user = currentUserService.getCurrentUser();

        Liability liability = liabilityRepository
                .findByIdAndUser(liabilityId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Liability not found"));

        LiabilityRepayment repayment =
                repaymentRepository
                        .findByIdAndLiability(repaymentId, liability)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Repayment not found"
                                ));

        return repaymentMapper.toResponse(repayment);
    }
}