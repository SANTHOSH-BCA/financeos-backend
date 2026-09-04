package com.financeos.financeosbackend.integration.networth;

import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import com.financeos.financeosbackend.integration.BaseIntegrationTest;
import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.enums.LiabilityType;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NetWorthIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should calculate net worth from recognized assets and liabilities")
    void getNetWorth_ShouldCalculateRecognizedValues() throws Exception {

        String token = createAuthenticatedUser();

        CreateAssetRequest assetRequest = new CreateAssetRequest();

        assetRequest.setAssetName("Personal Gold");
        assetRequest.setAssetType(AssetType.GOLD);
        assetRequest.setTotalValue(new BigDecimal("500000"));
        assetRequest.setOwnershipType(OwnershipType.INDIVIDUAL);
        assetRequest.setOwnershipPercentage(new BigDecimal("100"));
        assetRequest.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetRequest)))
                .andExpect(status().isCreated());

        CreateLiabilityRequest liabilityRequest =
                new CreateLiabilityRequest();

        liabilityRequest.setLiabilityName("Personal Loan");
        liabilityRequest.setLiabilityType(LiabilityType.PERSONAL_LOAN);
        liabilityRequest.setOutstandingAmount(new BigDecimal("150000"));
        liabilityRequest.setResponsibilityType(
                ResponsibilityType.INDIVIDUAL
        );
        liabilityRequest.setResponsibilityPercentage(
                new BigDecimal("100")
        );
        liabilityRequest.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/liabilities")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(liabilityRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v2/net-worth")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recognizedAssets").value(500000))
                .andExpect(jsonPath("$.data.recognizedLiabilities").value(150000))
                .andExpect(jsonPath("$.data.netWorth").value(350000));
    }

    @Test
    @DisplayName("Should exclude family unclear values from net worth")
    void getNetWorth_ShouldExcludeFamilyUnclearValues() throws Exception {

        String token = createAuthenticatedUser();

        CreateAssetRequest assetRequest = new CreateAssetRequest();

        assetRequest.setAssetName("Family Property");
        assetRequest.setAssetType(AssetType.LAND);
        assetRequest.setTotalValue(new BigDecimal("5000000"));
        assetRequest.setOwnershipType(OwnershipType.FAMILY_UNCLEAR);
        assetRequest.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetRequest)))
                .andExpect(status().isCreated());

        CreateLiabilityRequest liabilityRequest =
                new CreateLiabilityRequest();

        liabilityRequest.setLiabilityName("Family Loan");
        liabilityRequest.setLiabilityType(LiabilityType.OTHER);
        liabilityRequest.setOutstandingAmount(new BigDecimal("800000"));
        liabilityRequest.setResponsibilityType(
                ResponsibilityType.FAMILY_UNCLEAR
        );
        liabilityRequest.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/liabilities")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(liabilityRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v2/net-worth")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recognizedAssets").value(0))
                .andExpect(jsonPath("$.data.recognizedLiabilities").value(0))
                .andExpect(jsonPath("$.data.netWorth").value(0));
    }

    @Test
    @DisplayName("Should return only current user's financial values")
    void getNetWorth_ShouldReturnCurrentUsersValuesOnly() throws Exception {

        String userOneToken = createAuthenticatedUser();

        CreateAssetRequest assetRequest = new CreateAssetRequest();

        assetRequest.setAssetName("User One Gold");
        assetRequest.setAssetType(AssetType.GOLD);
        assetRequest.setTotalValue(new BigDecimal("100000"));
        assetRequest.setOwnershipType(OwnershipType.INDIVIDUAL);
        assetRequest.setOwnershipPercentage(new BigDecimal("100"));
        assetRequest.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(userOneToken))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetRequest)))
                .andExpect(status().isCreated());

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/v2/net-worth")
                        .header("Authorization", bearer(userTwoToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recognizedAssets").value(0))
                .andExpect(jsonPath("$.data.recognizedLiabilities").value(0))
                .andExpect(jsonPath("$.data.netWorth").value(0));
    }

    @Test
    @DisplayName("Should reject unauthorized net worth request")
    void getNetWorth_ShouldRejectUnauthorizedRequest() throws Exception {

        mockMvc.perform(get("/api/v2/net-worth"))
                .andExpect(status().isForbidden());
    }
}