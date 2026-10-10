package com.financeos.financeosbackend.insurance.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceAlreadyExistsException;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.insurance.dto.request.CreateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.request.UpdateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.response.InsurancePolicyResponse;
import com.financeos.financeosbackend.insurance.entity.InsurancePolicy;
import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
import com.financeos.financeosbackend.insurance.enums.InsuranceType;
import com.financeos.financeosbackend.insurance.mapper.InsurancePolicyMapper;
import com.financeos.financeosbackend.insurance.repository.InsurancePolicyRepository;
import com.financeos.financeosbackend.insurance.validator.InsurancePolicyValidator;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.insurance.enums.PremiumPaymentStatus;
import com.financeos.financeosbackend.insurance.dto.request.RenewInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.response.InsuranceRenewalResponse;
import com.financeos.financeosbackend.insurance.dto.response.ProtectionAnalysisItem;
import com.financeos.financeosbackend.insurance.entity.InsuranceRenewal;
import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;
import com.financeos.financeosbackend.insurance.mapper.InsuranceRenewalMapper;
import com.financeos.financeosbackend.insurance.repository.InsuranceRenewalRepository;

import java.math.BigDecimal;
import java.util.Objects;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsurancePolicyService {

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final CurrentUserService currentUserService;
    private final InsurancePolicyMapper insurancePolicyMapper;
    private final InsurancePolicyValidator insurancePolicyValidator;
    private final InsuranceRenewalRepository insuranceRenewalRepository;
    private final InsuranceRenewalMapper insuranceRenewalMapper;

    public InsurancePolicyService(
            InsurancePolicyRepository insurancePolicyRepository,
            CurrentUserService currentUserService,
            InsurancePolicyMapper insurancePolicyMapper,
            InsurancePolicyValidator insurancePolicyValidator,
            InsuranceRenewalRepository insuranceRenewalRepository,
            InsuranceRenewalMapper insuranceRenewalMapper
    ) {
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.currentUserService = currentUserService;
        this.insurancePolicyMapper = insurancePolicyMapper;
        this.insurancePolicyValidator = insurancePolicyValidator;
        this.insuranceRenewalRepository = insuranceRenewalRepository;
        this.insuranceRenewalMapper = insuranceRenewalMapper;
    }

    @Transactional
    public InsurancePolicyResponse createPolicy(CreateInsuranceRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        insurancePolicyValidator.validateCreate(request);
        validatePolicyNumber(request.policyNumber(), currentUser.getId(), null);

        InsurancePolicy policy = insurancePolicyMapper.toEntity(request);
        policy.setUser(currentUser);
        policy.setStatus(InsuranceStatus.PENDING);

        InsurancePolicy savedPolicy = insurancePolicyRepository.save(policy);

        return insurancePolicyMapper.toResponse(savedPolicy);
    }

    @Transactional(readOnly = true)
    public List<InsurancePolicyResponse> getMyPolicies() {

        User currentUser = currentUserService.getCurrentUser();

        return insurancePolicyRepository
                .findAllByUser_IdOrderByCreatedAtDesc(currentUser.getId())
                .stream()
                .map(insurancePolicyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InsurancePolicyResponse getMyPolicy(Long id) {

        InsurancePolicy policy = getPolicyForCurrentUser(id);

        return insurancePolicyMapper.toResponse(policy);
    }

    @Transactional
    public InsurancePolicyResponse updatePolicy(
            Long id,
            UpdateInsuranceRequest request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        InsurancePolicy existingPolicy = insurancePolicyRepository
                .findByIdAndUser_Id(id, currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Insurance policy not found")
                );

        insurancePolicyValidator.validateUpdate(request);

        validatePolicyNumber(
                request.policyNumber(),
                currentUser.getId(),
                existingPolicy.getId()
        );

        insurancePolicyMapper.updateEntity(existingPolicy, request);

        InsurancePolicy updatedPolicy =
                insurancePolicyRepository.save(existingPolicy);

        return insurancePolicyMapper.toResponse(updatedPolicy);
    }

    @Transactional
    public void deletePolicy(Long id) {

        InsurancePolicy policy = getPolicyForCurrentUser(id);

        insurancePolicyRepository.delete(policy);
    }

    private InsurancePolicy getPolicyForCurrentUser(Long id) {

        User currentUser = currentUserService.getCurrentUser();

        return insurancePolicyRepository
                .findByIdAndUser_Id(id, currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Insurance policy not found")
                );
    }

    private void validatePolicyNumber(
            String policyNumber,
            Long userId,
            Long currentPolicyId
    ) {
        if (policyNumber == null || policyNumber.isBlank()) {
            return;
        }

        boolean duplicate = insurancePolicyRepository
                .existsByPolicyNumberAndUser_Id(policyNumber.trim(), userId);

        if (!duplicate) {
            return;
        }

        if (currentPolicyId != null) {
            boolean belongsToCurrentPolicy = insurancePolicyRepository
                    .findByIdAndUser_Id(currentPolicyId, userId)
                    .map(policy -> policyNumber.trim().equals(policy.getPolicyNumber()))
                    .orElse(false);

            if (belongsToCurrentPolicy) {
                return;
            }
        }

        throw new ResourceAlreadyExistsException(
                "An insurance policy with this policy number already exists"
        );
    }

    @Transactional(readOnly = true)
    public List<InsurancePolicyResponse> getOverduePremiums() {

        User currentUser = currentUserService.getCurrentUser();

        LocalDate today = LocalDate.now();

        return insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        currentUser.getId(),
                        PremiumPaymentStatus.PENDING,
                        today.minusDays(1)
                )
                .stream()
                .map(insurancePolicyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InsurancePolicyResponse> getUpcomingPremiums() {

        User currentUser = currentUserService.getCurrentUser();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30);

        return insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        currentUser.getId(),
                        PremiumPaymentStatus.PENDING,
                        today,
                        endDate
                )
                .stream()
                .map(insurancePolicyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InsurancePolicyResponse> getExpiredPolicies() {

        User currentUser = currentUserService.getCurrentUser();

        LocalDate today = LocalDate.now();

        return insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBeforeOrderByExpiryDateAsc(
                        currentUser.getId(),
                        InsuranceStatus.ACTIVE,
                        today
                )
                .stream()
                .map(insurancePolicyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InsurancePolicyResponse> getPoliciesExpiringSoon() {

        User currentUser = currentUserService.getCurrentUser();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30);

        return insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        currentUser.getId(),
                        InsuranceStatus.ACTIVE,
                        today,
                        endDate
                )
                .stream()
                .map(insurancePolicyMapper::toResponse)
                .toList();
    }

    @Transactional
    public InsuranceRenewalResponse renewPolicy(
            Long policyId,
            RenewInsuranceRequest request
    ) {
        User currentUser = currentUserService.getCurrentUser();

        InsurancePolicy policy = insurancePolicyRepository
                .findByIdAndUser_Id(policyId, currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Insurance policy not found")
                );

        Objects.requireNonNull(request, "Renewal request is required");

        if (request.newStartDate() == null
                || request.newExpiryDate() == null) {
            throw new IllegalArgumentException(
                    "Renewal start date and expiry date are required"
            );
        }

        if (request.newExpiryDate().isBefore(request.newStartDate())) {
            throw new IllegalArgumentException(
                    "Renewal expiry date cannot be before the start date"
            );
        }

        if (request.newCoverageAmount() == null
                || request.newCoverageAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Renewal coverage amount must be greater than zero"
            );
        }

        if (request.newPremiumAmount() == null
                || request.newPremiumAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Renewal premium amount must be greater than zero"
            );
        }

        if (request.newPremiumFrequency() == null) {
            throw new IllegalArgumentException(
                    "Renewal premium frequency is required"
            );
        }

        if (policy.getStartDate() == null
                || policy.getCoverageAmount() == null
                || policy.getPremiumAmount() == null
                || policy.getPremiumFrequency() == null) {
            throw new IllegalStateException(
                    "Existing policy details are incomplete and cannot be renewed"
            );
        }

        InsuranceRenewal renewal = new InsuranceRenewal();
        renewal.setInsurancePolicy(policy);

        renewal.setPreviousStartDate(policy.getStartDate());
        renewal.setPreviousExpiryDate(policy.getExpiryDate());
        renewal.setNewStartDate(request.newStartDate());
        renewal.setNewExpiryDate(request.newExpiryDate());

        renewal.setPreviousCoverageAmount(policy.getCoverageAmount());
        renewal.setNewCoverageAmount(request.newCoverageAmount());

        renewal.setPreviousPremiumAmount(policy.getPremiumAmount());
        renewal.setNewPremiumAmount(request.newPremiumAmount());

        renewal.setPreviousPremiumFrequency(policy.getPremiumFrequency());
        renewal.setNewPremiumFrequency(request.newPremiumFrequency());

        InsuranceRenewal savedRenewal =
                insuranceRenewalRepository.save(renewal);

        policy.setStartDate(request.newStartDate());
        policy.setExpiryDate(request.newExpiryDate());
        policy.setCoverageAmount(request.newCoverageAmount());
        policy.setPremiumAmount(request.newPremiumAmount());
        policy.setPremiumFrequency(request.newPremiumFrequency());

        policy.setStatus(InsuranceStatus.ACTIVE);
        policy.setPremiumPaymentStatus(PremiumPaymentStatus.PENDING);
        policy.setNextPremiumDueDate(
                request.newStartDate().isBefore(LocalDate.now())
                        ? LocalDate.now()
                        : request.newStartDate()
        );

        insurancePolicyRepository.save(policy);

        return insuranceRenewalMapper.toResponse(savedRenewal);
    }

    @Transactional(readOnly = true)
    public List<InsuranceRenewalResponse> getRenewalHistory(Long policyId) {
        User currentUser = currentUserService.getCurrentUser();

        insurancePolicyRepository
                .findByIdAndUser_Id(policyId, currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Insurance policy not found")
                );

        return insuranceRenewalRepository
                .findAllByInsurancePolicy_IdAndInsurancePolicy_User_IdOrderByRenewedAtDesc(
                        policyId,
                        currentUser.getId()
                )
                .stream()
                .map(insuranceRenewalMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProtectionAnalysisItem> getProtectionAnalysis() {
        User currentUser = currentUserService.getCurrentUser();
        LocalDate today = LocalDate.now();

        List<InsurancePolicy> policies = insurancePolicyRepository
                .findAllByUser_IdOrderByCreatedAtDesc(currentUser.getId());

        List<ProtectionAnalysisItem> analysis = new java.util.ArrayList<>();

        analysis.add(analyseProtectionCategory(
                "HEALTH",
                policies,
                List.of(InsuranceType.HEALTH),
                today
        ));

        analysis.add(analyseProtectionCategory(
                "LIFE",
                policies,
                List.of(InsuranceType.LIFE, InsuranceType.TERM),
                today
        ));

        analysis.add(analyseProtectionCategory(
                "VEHICLE",
                policies,
                List.of(InsuranceType.VEHICLE),
                today
        ));

        return List.copyOf(analysis);
    }

    private ProtectionAnalysisItem analyseProtectionCategory(
            String category,
            List<InsurancePolicy> policies,
            List<InsuranceType> qualifyingTypes,
            LocalDate today
    ) {
        List<InsurancePolicy> categoryPolicies = policies.stream()
                .filter(policy -> policy.getInsuranceType() != null)
                .filter(policy -> qualifyingTypes.contains(policy.getInsuranceType()))
                .toList();

        int recordedPolicyCount = categoryPolicies.size();

        List<InsurancePolicy> effectivePolicies = categoryPolicies.stream()
                .filter(policy -> policy.getStatus() == InsuranceStatus.ACTIVE)
                .filter(policy -> policy.getStartDate() != null
                        && !policy.getStartDate().isAfter(today))
                .filter(policy -> policy.getExpiryDate() == null
                        || !policy.getExpiryDate().isBefore(today))
                .toList();

        if (recordedPolicyCount == 0) {
            return new ProtectionAnalysisItem(
                    category,
                    "GAP_IDENTIFIED",
                    0,
                    "No qualifying " + category.toLowerCase()
                            + " policy is recorded in FinanceOS. This does not confirm "
                            + "that you lack coverage outside the application."
            );
        }

        boolean incompleteData = categoryPolicies.stream()
                .anyMatch(policy ->
                        policy.getStatus() == null
                                || policy.getStartDate() == null
                                || policy.getCoverageAmount() == null
                                || policy.getCoverageAmount().compareTo(BigDecimal.ZERO) <= 0
                );

        if (!effectivePolicies.isEmpty()) {
            if (incompleteData) {
                return new ProtectionAnalysisItem(
                        category,
                        "INCOMPLETE_DATA",
                        effectivePolicies.size(),
                        "At least one qualifying policy is recorded as active, but some "
                                + "policy records have missing or invalid status, start date, "
                                + "or coverage amount. Review the recorded details."
                );
            }

            return new ProtectionAnalysisItem(
                    category,
                    "RECORDED",
                    effectivePolicies.size(),
                    "At least one qualifying policy is recorded as active and not past "
                            + "its recorded expiry date. This analysis does not assess "
                            + "whether the coverage amount is sufficient."
            );
        }

        boolean pendingPolicy = categoryPolicies.stream()
                .anyMatch(policy -> policy.getStatus() == InsuranceStatus.PENDING);

        if (pendingPolicy) {
            return new ProtectionAnalysisItem(
                    category,
                    "REVIEW_REQUIRED",
                    recordedPolicyCount,
                    "Qualifying policy records exist, but none currently qualifies as "
                            + "active protection. At least one is pending; confirm its "
                            + "approval and effective dates."
            );
        }

        return new ProtectionAnalysisItem(
                category,
                "GAP_IDENTIFIED",
                recordedPolicyCount,
                "Qualifying policy records exist, but none currently qualifies as active "
                        + "protection. Review their status and expiry dates."
        );
    }
}


