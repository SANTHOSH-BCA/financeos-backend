package com.financeos.financeosbackend.insurance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.filter.JwtAuthenticationFilter;
import com.financeos.financeosbackend.insurance.dto.request.CreateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.request.RenewInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.request.UpdateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.response.InsurancePolicyResponse;
import com.financeos.financeosbackend.insurance.dto.response.InsuranceRenewalResponse;
import com.financeos.financeosbackend.insurance.dto.response.ProtectionAnalysisItem;
import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
import com.financeos.financeosbackend.insurance.enums.InsuranceType;
import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;
import com.financeos.financeosbackend.insurance.enums.PremiumPaymentStatus;
import com.financeos.financeosbackend.insurance.service.InsurancePolicyService;
import com.financeos.financeosbackend.security.JwtService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(InsurancePolicyController.class)
@AutoConfigureMockMvc(addFilters = false)
class InsurancePolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InsurancePolicyService insurancePolicyService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

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
                LocalDate.of(2026, 11, 1),
                "Health insurance policy"
        );
    }

    private UpdateInsuranceRequest updateRequest() {
        return new UpdateInsuranceRequest(
                "Updated Health Protection",
                InsuranceType.HEALTH,
                "Example Insurance",
                "POLICY-1001",
                new BigDecimal("600000"),
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2026, 11, 1),
                InsuranceStatus.ACTIVE,
                "Updated policy details"
        );
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
                LocalDate.of(2026, 11, 1),
                InsuranceStatus.PENDING,
                "Health insurance policy",
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 10, 0),
                PremiumPaymentStatus.PENDING
        );
    }

    private RenewInsuranceRequest renewRequest() {
        return new RenewInsuranceRequest(
                LocalDate.of(2027, 1, 1),
                LocalDate.of(2027, 12, 31),
                new BigDecimal("750000"),
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY
        );
    }

    private InsuranceRenewalResponse renewalResponse() {
        return new InsuranceRenewalResponse(
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
                LocalDateTime.of(2026, 12, 1, 10, 0)
        );
    }

    @Test
    void createPolicy_ShouldReturnCreatedPolicy() throws Exception {
        InsurancePolicyResponse response = createResponse();

        when(insurancePolicyService.createPolicy(
                any(CreateInsuranceRequest.class)
        )).thenReturn(response);

        mockMvc.perform(post("/api/v2/insurance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.policyName").value("Health Protection"))
                .andExpect(jsonPath("$.insuranceType").value("HEALTH"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.premiumPaymentStatus").value("PENDING"));
    }

    @Test
    void getMyPolicies_ShouldReturnPolicies() throws Exception {
        when(insurancePolicyService.getMyPolicies())
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/v2/insurance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].policyName").value("Health Protection"))
                .andExpect(jsonPath("$[0].premiumPaymentStatus").value("PENDING"));
    }

    @Test
    void getMyPolicy_ShouldReturnPolicy() throws Exception {
        when(insurancePolicyService.getMyPolicy(1L))
                .thenReturn(createResponse());

        mockMvc.perform(get("/api/v2/insurance/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.provider").value("Example Insurance"))
                .andExpect(jsonPath("$.premiumPaymentStatus").value("PENDING"));
    }

    @Test
    void updatePolicy_ShouldReturnUpdatedPolicy() throws Exception {
        InsurancePolicyResponse response = new InsurancePolicyResponse(
                1L,
                "Updated Health Protection",
                InsuranceType.HEALTH,
                "Example Insurance",
                "POLICY-1001",
                new BigDecimal("600000"),
                new BigDecimal("15000"),
                PremiumFrequency.YEARLY,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2026, 11, 1),
                InsuranceStatus.ACTIVE,
                "Updated policy details",
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 2, 10, 0),
                PremiumPaymentStatus.PENDING
        );

        when(insurancePolicyService.updatePolicy(
                eq(1L),
                any(UpdateInsuranceRequest.class)
        )).thenReturn(response);

        mockMvc.perform(put("/api/v2/insurance/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyName")
                        .value("Updated Health Protection"))
                .andExpect(jsonPath("$.coverageAmount").value(600000))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.premiumPaymentStatus").value("PENDING"));
    }

    @Test
    void deletePolicy_ShouldReturnNoContent() throws Exception {
        doNothing().when(insurancePolicyService).deletePolicy(1L);

        mockMvc.perform(delete("/api/v2/insurance/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createPolicy_ShouldReturnBadRequest_WhenPolicyNameIsMissing()
            throws Exception {

        CreateInsuranceRequest invalidRequest = new CreateInsuranceRequest(
                "",
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

        mockMvc.perform(post("/api/v2/insurance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPolicy_ShouldReturnBadRequest_WhenCoverageIsZero()
            throws Exception {

        CreateInsuranceRequest invalidRequest = new CreateInsuranceRequest(
                "Health Protection",
                InsuranceType.HEALTH,
                "Example Insurance",
                "POLICY-1001",
                BigDecimal.ZERO,
                new BigDecimal("12000"),
                PremiumFrequency.YEARLY,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                null,
                null
        );

        mockMvc.perform(post("/api/v2/insurance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOverduePremiums_ShouldReturnPolicies() throws Exception {
        when(insurancePolicyService.getOverduePremiums())
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/v2/insurance/premiums/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].policyName").value("Health Protection"))
                .andExpect(jsonPath("$[0].premiumPaymentStatus").value("PENDING"));
    }

    @Test
    void getUpcomingPremiums_ShouldReturnPolicies() throws Exception {
        when(insurancePolicyService.getUpcomingPremiums())
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/v2/insurance/premiums/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].policyName").value("Health Protection"))
                .andExpect(jsonPath("$[0].premiumPaymentStatus").value("PENDING"));
    }

    @Test
    void getExpiredPolicies_ShouldReturnPolicies() throws Exception {
        when(insurancePolicyService.getExpiredPolicies())
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/v2/insurance/expired"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].policyName").value("Health Protection"))
                .andExpect(jsonPath("$[0].premiumPaymentStatus").value("PENDING"));
    }

    @Test
    void getPoliciesExpiringSoon_ShouldReturnPolicies() throws Exception {
        when(insurancePolicyService.getPoliciesExpiringSoon())
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/api/v2/insurance/expiring-soon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].policyName").value("Health Protection"))
                .andExpect(jsonPath("$[0].premiumPaymentStatus").value("PENDING"));
    }

    @Test
    void renewPolicy_ShouldReturnCreatedRenewal() throws Exception {
        when(insurancePolicyService.renewPolicy(
                eq(1L),
                any(RenewInsuranceRequest.class)
        )).thenReturn(renewalResponse());

        mockMvc.perform(post("/api/v2/insurance/1/renew")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(renewRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.previousCoverageAmount").value(500000))
                .andExpect(jsonPath("$.newCoverageAmount").value(750000))
                .andExpect(jsonPath("$.newPremiumAmount").value(15000))
                .andExpect(jsonPath("$.newPremiumFrequency").value("YEARLY"));
    }

    @Test
    void getRenewalHistory_ShouldReturnRenewals() throws Exception {
        when(insurancePolicyService.getRenewalHistory(1L))
                .thenReturn(List.of(renewalResponse()));

        mockMvc.perform(get("/api/v2/insurance/1/renewals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].previousCoverageAmount").value(500000))
                .andExpect(jsonPath("$[0].newCoverageAmount").value(750000))
                .andExpect(jsonPath("$[0].newPremiumFrequency").value("YEARLY"));
    }

    @Test
    void renewPolicy_ShouldReturnBadRequest_WhenNewCoverageIsMissing()
            throws Exception {

        String invalidRequest = """
                {
                  "newStartDate": "2027-01-01",
                  "newExpiryDate": "2027-12-31",
                  "newPremiumAmount": 15000,
                  "newPremiumFrequency": "YEARLY"
                }
                """;

        mockMvc.perform(post("/api/v2/insurance/1/renew")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProtectionAnalysis_ShouldReturnCategories() throws Exception {
        when(insurancePolicyService.getProtectionAnalysis()).thenReturn(List.of(
                new ProtectionAnalysisItem(
                        "HEALTH", "RECORDED", 1,
                        "A qualifying policy is recorded."
                ),
                new ProtectionAnalysisItem(
                        "LIFE", "GAP_IDENTIFIED", 0,
                        "No qualifying life policy is recorded in FinanceOS."
                ),
                new ProtectionAnalysisItem(
                        "VEHICLE", "GAP_IDENTIFIED", 0,
                        "No qualifying vehicle policy is recorded in FinanceOS."
                )
        ));

        mockMvc.perform(get("/api/v2/insurance/protection-analysis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].category").value("HEALTH"))
                .andExpect(jsonPath("$[0].status").value("RECORDED"))
                .andExpect(jsonPath("$[0].recordedPolicyCount").value(1))
                .andExpect(jsonPath("$[1].category").value("LIFE"))
                .andExpect(jsonPath("$[2].category").value("VEHICLE"));
    }

    @Test
    void getProtectionAnalysis_WhenNoItems_ShouldReturnEmptyArray()
            throws Exception {
        when(insurancePolicyService.getProtectionAnalysis())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/insurance/protection-analysis"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}


