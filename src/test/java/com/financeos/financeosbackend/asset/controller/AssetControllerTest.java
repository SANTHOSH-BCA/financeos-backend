package com.financeos.financeosbackend.asset.controller;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.asset.dto.AssetResponse;
import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import com.financeos.financeosbackend.asset.service.AssetService;
import com.financeos.financeosbackend.filter.JwtAuthenticationFilter;
import com.financeos.financeosbackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssetController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AssetService assetService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void createAsset_ShouldReturnCreatedAsset() throws Exception {

        CreateAssetRequest request = new CreateAssetRequest();
        request.setAssetName("Family Gold");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(new BigDecimal("200000"));
        request.setOwnershipType(OwnershipType.SHARED);
        request.setOwnershipPercentage(new BigDecimal("25"));
        request.setValuationDate(LocalDate.of(2026, 8, 31));

        AssetResponse response = new AssetResponse();
        response.setId(1L);
        response.setAssetName("Family Gold");
        response.setAssetType(AssetType.GOLD);
        response.setTotalValue(new BigDecimal("200000"));
        response.setOwnershipType(OwnershipType.SHARED);
        response.setOwnershipPercentage(new BigDecimal("25"));
        response.setRecognizedValue(new BigDecimal("50000"));
        response.setIncludedInNetWorth(true);
        response.setValuationDate(LocalDate.of(2026, 8, 31));

        when(assetService.createAsset(any(CreateAssetRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v2/assets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.assetName").value("Family Gold"))
                .andExpect(jsonPath("$.assetType").value("GOLD"))
                .andExpect(jsonPath("$.totalValue").value(200000))
                .andExpect(jsonPath("$.ownershipType").value("SHARED"))
                .andExpect(jsonPath("$.ownershipPercentage").value(25))
                .andExpect(jsonPath("$.recognizedValue").value(50000))
                .andExpect(jsonPath("$.includedInNetWorth").value(true));
    }

    @Test
    void getMyAssets_ShouldReturnAssets() throws Exception {

        AssetResponse response = new AssetResponse();
        response.setId(1L);
        response.setAssetName("Personal Gold");
        response.setAssetType(AssetType.GOLD);
        response.setTotalValue(new BigDecimal("100000"));
        response.setOwnershipType(OwnershipType.INDIVIDUAL);
        response.setOwnershipPercentage(new BigDecimal("100"));
        response.setRecognizedValue(new BigDecimal("100000"));
        response.setIncludedInNetWorth(true);

        when(assetService.getMyAssets())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].assetName").value("Personal Gold"))
                .andExpect(jsonPath("$[0].ownershipType").value("INDIVIDUAL"))
                .andExpect(jsonPath("$[0].recognizedValue").value(100000));
    }

    @Test
    void getMyAsset_ShouldReturnAsset() throws Exception {

        AssetResponse response = new AssetResponse();
        response.setId(1L);
        response.setAssetName("Family Land");
        response.setAssetType(AssetType.LAND);
        response.setTotalValue(new BigDecimal("5000000"));
        response.setOwnershipType(OwnershipType.SHARED);
        response.setOwnershipPercentage(new BigDecimal("25"));
        response.setRecognizedValue(new BigDecimal("1250000"));
        response.setIncludedInNetWorth(true);

        when(assetService.getMyAsset(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/assets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.assetName").value("Family Land"))
                .andExpect(jsonPath("$.assetType").value("LAND"))
                .andExpect(jsonPath("$.recognizedValue").value(1250000));
    }

    @Test
    void createAsset_ShouldFailValidation_WhenAssetNameIsMissing()
            throws Exception {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(new BigDecimal("200000"));
        request.setOwnershipType(OwnershipType.SHARED);
        request.setOwnershipPercentage(new BigDecimal("25"));
        request.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/assets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAsset_ShouldFailValidation_WhenTotalValueIsInvalid()
            throws Exception {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("Gold");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(BigDecimal.ZERO);
        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(new BigDecimal("100"));
        request.setValuationDate(LocalDate.now());

        mockMvc.perform(post("/api/v2/assets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}