package com.financeos.financeosbackend.integration.asset;

import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.entity.Asset;
import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import com.financeos.financeosbackend.integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AssetIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should create individual asset successfully")
    void createAsset_ShouldCreateIndividualAsset() throws Exception {

        String token = createAuthenticatedUser();

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("Personal Gold");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(new BigDecimal("200000"));
        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(new BigDecimal("100"));
        request.setValuationDate(LocalDate.of(2026, 8, 31));

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.assetName").value("Personal Gold"))
                .andExpect(jsonPath("$.assetType").value("GOLD"))
                .andExpect(jsonPath("$.totalValue").value(200000))
                .andExpect(jsonPath("$.ownershipType").value("INDIVIDUAL"))
                .andExpect(jsonPath("$.ownershipPercentage").value(100))
                .andExpect(jsonPath("$.recognizedValue").value(200000))
                .andExpect(jsonPath("$.includedInNetWorth").value(true));
    }

    @Test
    @DisplayName("Should calculate shared asset ownership value")
    void createAsset_ShouldCalculateSharedOwnershipValue() throws Exception {

        String token = createAuthenticatedUser();

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("Family Gold");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(new BigDecimal("200000"));
        request.setOwnershipType(OwnershipType.SHARED);
        request.setOwnershipPercentage(new BigDecimal("25"));
        request.setValuationDate(LocalDate.of(2026, 8, 31));

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalValue").value(200000))
                .andExpect(jsonPath("$.ownershipPercentage").value(25))
                .andExpect(jsonPath("$.recognizedValue").value(50000))
                .andExpect(jsonPath("$.includedInNetWorth").value(true));
    }

    @Test
    @DisplayName("Should exclude family unclear asset from personal net worth")
    void createAsset_ShouldExcludeFamilyUnclearAsset() throws Exception {

        String token = createAuthenticatedUser();

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("Family Property");
        request.setAssetType(AssetType.LAND);
        request.setTotalValue(new BigDecimal("5000000"));
        request.setOwnershipType(OwnershipType.FAMILY_UNCLEAR);
        request.setValuationDate(LocalDate.of(2026, 8, 31));

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ownershipType").value("FAMILY_UNCLEAR"))
                .andExpect(jsonPath("$.ownershipPercentage").doesNotExist())
                .andExpect(jsonPath("$.recognizedValue").value(0))
                .andExpect(jsonPath("$.includedInNetWorth").value(false));
    }

    @Test
    @DisplayName("Should return only current user's assets")
    void getAssets_ShouldReturnCurrentUsersAssetsOnly() throws Exception {

        String userOneToken = createAuthenticatedUser();

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("User One Gold");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(new BigDecimal("100000"));
        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(new BigDecimal("100"));
        request.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(userOneToken))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/v2/assets")
                        .header("Authorization", bearer(userTwoToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should reject unauthorized asset access")
    void getAssets_ShouldRejectUnauthorizedRequest() throws Exception {

        mockMvc.perform(get("/api/v2/assets"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should reject invalid asset request")
    void createAsset_ShouldRejectInvalidRequest() throws Exception {

        String token = createAuthenticatedUser();

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(BigDecimal.ZERO);
        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(new BigDecimal("100"));
        request.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should prevent user from accessing another user's asset")
    void getAsset_ShouldRejectAnotherUsersAsset() throws Exception {

        String userOneToken = createAuthenticatedUser();

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("Private Gold");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(new BigDecimal("150000"));
        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(new BigDecimal("100"));
        request.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/assets")
                        .header("Authorization", bearer(userOneToken))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Asset asset = assetRepository.findAll()
                .stream()
                .filter(savedAsset ->
                        "Private Gold".equals(savedAsset.getAssetName()))
                .findFirst()
                .orElseThrow();

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/v2/assets/" + asset.getId())
                        .header("Authorization", bearer(userTwoToken)))
                .andExpect(status().isNotFound());
    }
}