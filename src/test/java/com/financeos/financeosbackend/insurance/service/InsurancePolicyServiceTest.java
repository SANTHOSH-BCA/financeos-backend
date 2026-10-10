package com.financeos.financeosbackend.insurance.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceAlreadyExistsException;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.insurance.dto.request.CreateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.response.InsurancePolicyResponse;
import com.financeos.financeosbackend.insurance.dto.response.ProtectionAnalysisItem;
import com.financeos.financeosbackend.insurance.dto.response.InsuranceRenewalResponse;
import com.financeos.financeosbackend.insurance.entity.InsurancePolicy;
import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
import com.financeos.financeosbackend.insurance.enums.InsuranceType;
import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;
import com.financeos.financeosbackend.insurance.enums.PremiumPaymentStatus;
import com.financeos.financeosbackend.insurance.mapper.InsurancePolicyMapper;
import com.financeos.financeosbackend.insurance.repository.InsurancePolicyRepository;
import com.financeos.financeosbackend.insurance.validator.InsurancePolicyValidator;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.insurance.dto.request.RenewInsuranceRequest;
import com.financeos.financeosbackend.insurance.entity.InsuranceRenewal;
import com.financeos.financeosbackend.insurance.mapper.InsuranceRenewalMapper;
import com.financeos.financeosbackend.insurance.repository.InsuranceRenewalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.eq;
@ExtendWith(MockitoExtension.class)
class InsurancePolicyServiceTest {

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private InsurancePolicyMapper insurancePolicyMapper;

    @Mock
    private InsurancePolicyValidator insurancePolicyValidator;

    @Mock
    private InsuranceRenewalRepository insuranceRenewalRepository;

    @Mock
    private InsuranceRenewalMapper insuranceRenewalMapper;

    @InjectMocks
    private InsurancePolicyService insurancePolicyService;

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        return user;
    }

    private InsurancePolicy createPolicy(User user) {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setUser(user);
        policy.setPolicyName("Health Protection");
        policy.setInsuranceType(InsuranceType.HEALTH);
        policy.setProvider("Example Insurance");
        policy.setPolicyNumber("POLICY-1001");
        policy.setCoverageAmount(new BigDecimal("500000"));
        policy.setPremiumAmount(new BigDecimal("12000"));
        policy.setPremiumFrequency(PremiumFrequency.YEARLY);
        policy.setStartDate(LocalDate.of(2026, 1, 1));
        policy.setExpiryDate(LocalDate.of(2026, 12, 31));
        policy.setStatus(InsuranceStatus.PENDING);
        policy.setPremiumPaymentStatus(PremiumPaymentStatus.PENDING);
        return policy;
    }

    private InsurancePolicyResponse createResponse() {
        return new InsurancePolicyResponse(
                1L,
                "Health Protection",
                InsuranceType.HEALTH,
                "Example Insurance",
                "POLICY-1001",
                new BigDecimal("500000"),
                new BigDecimal("12000"),
                PremiumFrequency.YEARLY,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                null,
                InsuranceStatus.PENDING,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                PremiumPaymentStatus.PENDING
        );
    }

    private CreateInsuranceRequest createRequest() {
        return new CreateInsuranceRequest(
                "Health Protection",
                InsuranceType.HEALTH,
                "Example Insurance",
                "POLICY-1001",
                new BigDecimal("500000"),
                new BigDecimal("12000"),
                PremiumFrequency.YEARLY,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                null,
                null
        );
    }

    @Test
    void createPolicy_ShouldCreateSuccessfully() {
        User user = createUser();
        CreateInsuranceRequest request = createRequest();
        InsurancePolicy policy = createPolicy(user);
        InsurancePolicy savedPolicy = createPolicy(user);
        InsurancePolicyResponse response = createResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyMapper.toEntity(request)).thenReturn(policy);
        when(insurancePolicyRepository.save(policy)).thenReturn(savedPolicy);
        when(insurancePolicyMapper.toResponse(savedPolicy)).thenReturn(response);

        InsurancePolicyResponse result =
                insurancePolicyService.createPolicy(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Health Protection", result.policyName());
        assertEquals(user, policy.getUser());
        assertEquals(InsuranceStatus.PENDING, policy.getStatus());
        assertEquals(
                PremiumPaymentStatus.PENDING,
                policy.getPremiumPaymentStatus()
        );

        verify(insurancePolicyValidator).validateCreate(request);
        verify(insurancePolicyRepository).save(policy);
        verify(insurancePolicyMapper).toResponse(savedPolicy);
    }

    @Test
    void getMyPolicies_ShouldReturnCurrentUsersPolicies() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        InsurancePolicyResponse response = createResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(policy));

        when(insurancePolicyMapper.toResponse(policy)).thenReturn(response);

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getMyPolicies();

        assertEquals(1, result.size());
        assertEquals("Health Protection", result.get(0).policyName());
        assertEquals(
                PremiumPaymentStatus.PENDING,
                result.get(0).premiumPaymentStatus()
        );

        verify(insurancePolicyRepository)
                .findAllByUser_IdOrderByCreatedAtDesc(1L);
    }

    @Test
    void getMyPolicy_WhenPolicyDoesNotBelongToUser_ShouldThrowNotFound() {
        User user = createUser();

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository.findByIdAndUser_Id(99L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> insurancePolicyService.getMyPolicy(99L)
        );

        verify(insurancePolicyMapper, never()).toResponse(any());
    }

    @Test
    void createPolicy_WhenPolicyNumberAlreadyExists_ShouldThrowException() {
        User user = createUser();
        CreateInsuranceRequest request = createRequest();

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .existsByPolicyNumberAndUser_Id("POLICY-1001", 1L))
                .thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> insurancePolicyService.createPolicy(request)
        );

        verify(insurancePolicyRepository, never()).save(any());
    }

    @Test
    void deletePolicy_ShouldDeletePolicyBelongingToCurrentUser() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(policy));

        insurancePolicyService.deletePolicy(1L);

        verify(insurancePolicyRepository).delete(policy);
    }

    @Test
    void getOverduePremiums_ShouldReturnPoliciesDueBeforeToday() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        InsurancePolicyResponse response = createResponse();

        LocalDate today = LocalDate.now();
        LocalDate lastOverdueDate = today.minusDays(1);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        eq(1L),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(lastOverdueDate)
                ))
                .thenReturn(List.of(policy));

        when(insurancePolicyMapper.toResponse(policy)).thenReturn(response);

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getOverduePremiums();

        assertEquals(1, result.size());
        assertEquals("Health Protection", result.get(0).policyName());

        verify(insurancePolicyRepository)
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        1L,
                        PremiumPaymentStatus.PENDING,
                        lastOverdueDate
                );
    }

    @Test
    void getOverduePremiums_WhenNoOverduePremiums_ShouldReturnEmptyList() {
        User user = createUser();

        LocalDate lastOverdueDate = LocalDate.now().minusDays(1);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        1L,
                        PremiumPaymentStatus.PENDING,
                        lastOverdueDate
                ))
                .thenReturn(List.of());

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getOverduePremiums();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(insurancePolicyMapper, never()).toResponse(any());
    }

    @Test
    void getUpcomingPremiums_ShouldReturnPoliciesDueWithinNext30Days() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        InsurancePolicyResponse response = createResponse();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        eq(1L),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(today),
                        eq(endDate)
                ))
                .thenReturn(List.of(policy));

        when(insurancePolicyMapper.toResponse(policy)).thenReturn(response);

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getUpcomingPremiums();

        assertEquals(1, result.size());
        assertEquals("Health Protection", result.get(0).policyName());

        verify(insurancePolicyRepository)
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        1L,
                        PremiumPaymentStatus.PENDING,
                        today,
                        endDate
                );
    }

    @Test
    void getUpcomingPremiums_WhenNoUpcomingPremiums_ShouldReturnEmptyList() {
        User user = createUser();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        1L,
                        PremiumPaymentStatus.PENDING,
                        today,
                        endDate
                ))
                .thenReturn(List.of());

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getUpcomingPremiums();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(insurancePolicyMapper, never()).toResponse(any());
    }

    @Test
    void getExpiredPolicies_ShouldReturnExpiredActivePolicies() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        InsurancePolicyResponse response = createResponse();

        LocalDate today = LocalDate.now();

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBeforeOrderByExpiryDateAsc(
                        1L,
                        InsuranceStatus.ACTIVE,
                        today
                ))
                .thenReturn(List.of(policy));

        when(insurancePolicyMapper.toResponse(policy)).thenReturn(response);

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getExpiredPolicies();

        assertEquals(1, result.size());
        assertEquals("Health Protection", result.get(0).policyName());

        verify(insurancePolicyRepository)
                .findAllByUser_IdAndStatusAndExpiryDateBeforeOrderByExpiryDateAsc(
                        1L,
                        InsuranceStatus.ACTIVE,
                        today
                );
    }

    @Test
    void getExpiredPolicies_WhenNoExpiredPolicies_ShouldReturnEmptyList() {
        User user = createUser();

        LocalDate today = LocalDate.now();

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBeforeOrderByExpiryDateAsc(
                        1L,
                        InsuranceStatus.ACTIVE,
                        today
                ))
                .thenReturn(List.of());

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getExpiredPolicies();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(insurancePolicyMapper, never()).toResponse(any());
    }

    @Test
    void getPoliciesExpiringSoon_ShouldReturnPoliciesWithinNext30Days() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        InsurancePolicyResponse response = createResponse();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        1L,
                        InsuranceStatus.ACTIVE,
                        today,
                        endDate
                ))
                .thenReturn(List.of(policy));

        when(insurancePolicyMapper.toResponse(policy)).thenReturn(response);

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getPoliciesExpiringSoon();

        assertEquals(1, result.size());
        assertEquals("Health Protection", result.get(0).policyName());

        verify(insurancePolicyRepository)
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        1L,
                        InsuranceStatus.ACTIVE,
                        today,
                        endDate
                );
    }

    @Test
    void getPoliciesExpiringSoon_WhenNoPolicies_ShouldReturnEmptyList() {
        User user = createUser();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        1L,
                        InsuranceStatus.ACTIVE,
                        today,
                        endDate
                ))
                .thenReturn(List.of());

        List<InsurancePolicyResponse> result =
                insurancePolicyService.getPoliciesExpiringSoon();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(insurancePolicyMapper, never()).toResponse(any());
    }

    @Test
    void renewPolicy_ShouldSaveHistoryAndUpdatePolicy() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);

        RenewInsuranceRequest request = new RenewInsuranceRequest(
                LocalDate.of(2027, 1, 1),
                LocalDate.of(2027, 12, 31),
                new BigDecimal("750000"),
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY
        );

        InsuranceRenewalResponse response = new InsuranceRenewalResponse(
                1L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2027, 1, 1),
                LocalDate.of(2027, 12, 31),
                new BigDecimal("500000"),
                new BigDecimal("750000"),
                new BigDecimal("12000"),
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY,
                PremiumFrequency.YEARLY,
                LocalDateTime.now()
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(policy));
        when(insuranceRenewalRepository.save(any(InsuranceRenewal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(insuranceRenewalMapper.toResponse(any(InsuranceRenewal.class)))
                .thenReturn(response);

        InsuranceRenewalResponse result =
                insurancePolicyService.renewPolicy(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(new BigDecimal("750000"), policy.getCoverageAmount());
        assertEquals(new BigDecimal("15000"), policy.getPremiumAmount());
        assertEquals(PremiumFrequency.YEARLY, policy.getPremiumFrequency());
        assertEquals(LocalDate.of(2027, 1, 1), policy.getStartDate());
        assertEquals(LocalDate.of(2027, 12, 31), policy.getExpiryDate());
        assertEquals(InsuranceStatus.ACTIVE, policy.getStatus());
        assertEquals(
                PremiumPaymentStatus.PENDING,
                policy.getPremiumPaymentStatus()
        );

        verify(insuranceRenewalRepository).save(any(InsuranceRenewal.class));
        verify(insurancePolicyRepository).save(policy);
        verify(insuranceRenewalMapper).toResponse(any(InsuranceRenewal.class));
    }

    @Test
    void renewPolicy_WhenPolicyDoesNotBelongToUser_ShouldThrowNotFound() {
        User user = createUser();

        RenewInsuranceRequest request = new RenewInsuranceRequest(
                LocalDate.of(2027, 1, 1),
                LocalDate.of(2027, 12, 31),
                new BigDecimal("750000"),
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findByIdAndUser_Id(99L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> insurancePolicyService.renewPolicy(99L, request)
        );

        verify(insuranceRenewalRepository, never()).save(any());
        verify(insurancePolicyRepository, never()).save(any());
    }

    @Test
    void renewPolicy_WhenExpiryDateIsBeforeStartDate_ShouldThrowException() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);

        RenewInsuranceRequest request = new RenewInsuranceRequest(
                LocalDate.of(2027, 12, 31),
                LocalDate.of(2027, 1, 1),
                new BigDecimal("750000"),
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(policy));

        assertThrows(
                IllegalArgumentException.class,
                () -> insurancePolicyService.renewPolicy(1L, request)
        );

        verify(insuranceRenewalRepository, never()).save(any());
        verify(insurancePolicyRepository, never()).save(any());
    }

    @Test
    void renewPolicy_WhenCoverageAmountIsZero_ShouldThrowException() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);

        RenewInsuranceRequest request = new RenewInsuranceRequest(
                LocalDate.of(2027, 1, 1),
                LocalDate.of(2027, 12, 31),
                BigDecimal.ZERO,
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(policy));

        assertThrows(
                IllegalArgumentException.class,
                () -> insurancePolicyService.renewPolicy(1L, request)
        );

        verify(insuranceRenewalRepository, never()).save(any());
        verify(insurancePolicyRepository, never()).save(any());
    }

    @Test
    void getRenewalHistory_ShouldReturnHistoryForCurrentUsersPolicy() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        InsuranceRenewal renewal = new InsuranceRenewal();

        InsuranceRenewalResponse response = new InsuranceRenewalResponse(
                1L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2027, 1, 1),
                LocalDate.of(2027, 12, 31),
                new BigDecimal("500000"),
                new BigDecimal("750000"),
                new BigDecimal("12000"),
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY,
                PremiumFrequency.YEARLY,
                LocalDateTime.now()
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(policy));
        when(insuranceRenewalRepository
                .findAllByInsurancePolicy_IdAndInsurancePolicy_User_IdOrderByRenewedAtDesc(
                        1L, 1L
                ))
                .thenReturn(List.of(renewal));
        when(insuranceRenewalMapper.toResponse(renewal)).thenReturn(response);

        List<InsuranceRenewalResponse> result =
                insurancePolicyService.getRenewalHistory(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(
                new BigDecimal("750000"),
                result.get(0).newCoverageAmount()
        );

        verify(insuranceRenewalRepository)
                .findAllByInsurancePolicy_IdAndInsurancePolicy_User_IdOrderByRenewedAtDesc(
                        1L, 1L
                );
    }

    @Test
    void getProtectionAnalysis_WhenNoPolicies_ShouldIdentifyGap() {
        User user = createUser();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findAllByUser_IdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of());

        List<ProtectionAnalysisItem> result =
                insurancePolicyService.getProtectionAnalysis();

        assertEquals(3, result.size());
        assertEquals("HEALTH", result.get(0).category());
        assertEquals("GAP_IDENTIFIED", result.get(0).status());
        assertEquals(0, result.get(0).recordedPolicyCount());
        assertTrue(result.get(0).explanation().contains("recorded in FinanceOS"));
        assertEquals("LIFE", result.get(1).category());
        assertEquals("VEHICLE", result.get(2).category());
    }

    @Test
    void getProtectionAnalysis_WithActiveHealthPolicy_ShouldReportRecorded() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        policy.setStatus(InsuranceStatus.ACTIVE);
        policy.setExpiryDate(LocalDate.now().plusDays(60));

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findAllByUser_IdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(policy));

        List<ProtectionAnalysisItem> result =
                insurancePolicyService.getProtectionAnalysis();

        assertEquals("RECORDED", result.get(0).status());
        assertEquals(1, result.get(0).recordedPolicyCount());
        assertTrue(result.get(0).explanation().contains("does not assess"));
    }

    @Test
    void getProtectionAnalysis_WithFutureDatedActiveHealthPolicy_ShouldNotReportRecorded() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        policy.setStatus(InsuranceStatus.ACTIVE);
        policy.setStartDate(LocalDate.now().plusDays(10));
        policy.setExpiryDate(LocalDate.now().plusDays(365));

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findAllByUser_IdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(policy));

        List<ProtectionAnalysisItem> result =
                insurancePolicyService.getProtectionAnalysis();

        ProtectionAnalysisItem healthAnalysis = result.stream()
                .filter(item -> item.category().equals("HEALTH"))
                .findFirst()
                .orElseThrow();

        assertEquals("GAP_IDENTIFIED", healthAnalysis.status());
        assertTrue(healthAnalysis.explanation()
                .contains("none currently qualifies as active"));
    }
    @Test
    void getProtectionAnalysis_WithExpiredHealthPolicy_ShouldIdentifyGap() {
        User user = createUser();
        InsurancePolicy policy = createPolicy(user);
        policy.setStatus(InsuranceStatus.ACTIVE);
        policy.setExpiryDate(LocalDate.now().minusDays(1));

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(insurancePolicyRepository.findAllByUser_IdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(policy));

        List<ProtectionAnalysisItem> result =
                insurancePolicyService.getProtectionAnalysis();

        assertEquals("GAP_IDENTIFIED", result.get(0).status());
        assertEquals(1, result.get(0).recordedPolicyCount());
        assertTrue(result.get(0).explanation().contains("expiry dates"));
    }
}



