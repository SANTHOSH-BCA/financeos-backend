package com.financeos.financeosbackend.liability.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.asset.validator.AssetValidator;
import com.financeos.financeosbackend.filter.JwtAuthenticationFilter;
import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityResponse;
import com.financeos.financeosbackend.liability.enums.LiabilityType;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import com.financeos.financeosbackend.liability.service.LiabilityService;
import com.financeos.financeosbackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LiabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
class LiabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LiabilityService liabilityService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void createLiability_ShouldReturnCreatedResponse() throws Exception {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setLiabilityName("Home Loan");
        request.setLiabilityType(LiabilityType.HOME_LOAN);
        request.setOutstandingAmount(
                new BigDecimal("2000000")
        );
        request.setResponsibilityType(
                ResponsibilityType.SHARED
        );
        request.setResponsibilityPercentage(
                new BigDecimal("70")
        );
        request.setValuationDate(
                LocalDate.of(2026, 8, 31)
        );

        LiabilityResponse response =
                new LiabilityResponse();

        response.setId(1L);
        response.setLiabilityName("Home Loan");
        response.setLiabilityType(
                LiabilityType.HOME_LOAN
        );
        response.setOutstandingAmount(
                new BigDecimal("2000000")
        );
        response.setResponsibilityType(
                ResponsibilityType.SHARED
        );
        response.setResponsibilityPercentage(
                new BigDecimal("70")
        );
        response.setRecognizedLiability(
                new BigDecimal("1400000.00")
        );
        response.setIncludedInNetWorth(true);
        response.setValuationDate(
                LocalDate.of(2026, 8, 31)
        );

        when(liabilityService.createLiability(
                any(CreateLiabilityRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        post("/api/v2/liabilities")
                                .contentType(APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(
                        jsonPath("$.liabilityName")
                                .value("Home Loan")
                )
                .andExpect(
                        jsonPath("$.liabilityType")
                                .value("HOME_LOAN")
                )
                .andExpect(
                        jsonPath("$.outstandingAmount")
                                .value(2000000)
                )
                .andExpect(
                        jsonPath("$.responsibilityType")
                                .value("SHARED")
                )
                .andExpect(
                        jsonPath("$.responsibilityPercentage")
                                .value(70)
                )
                .andExpect(
                        jsonPath("$.recognizedLiability")
                                .value(1400000)
                )
                .andExpect(
                        jsonPath("$.includedInNetWorth")
                                .value(true)
                );
    }

    @Test
    void getMyLiabilities_ShouldReturnLiabilities() throws Exception {

        LiabilityResponse response =
                new LiabilityResponse();

        response.setId(1L);
        response.setLiabilityName("Personal Loan");
        response.setLiabilityType(
                LiabilityType.PERSONAL_LOAN
        );
        response.setOutstandingAmount(
                new BigDecimal("500000")
        );
        response.setResponsibilityType(
                ResponsibilityType.INDIVIDUAL
        );
        response.setResponsibilityPercentage(
                new BigDecimal("100")
        );
        response.setRecognizedLiability(
                new BigDecimal("500000.00")
        );
        response.setIncludedInNetWorth(true);
        response.setValuationDate(
                LocalDate.of(2026, 8, 31)
        );

        when(liabilityService.getMyLiabilities())
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v2/liabilities")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(
                        jsonPath("$[0].liabilityName")
                                .value("Personal Loan")
                )
                .andExpect(
                        jsonPath("$[0].liabilityType")
                                .value("PERSONAL_LOAN")
                )
                .andExpect(
                        jsonPath("$[0].responsibilityType")
                                .value("INDIVIDUAL")
                )
                .andExpect(
                        jsonPath("$[0].responsibilityPercentage")
                                .value(100)
                )
                .andExpect(
                        jsonPath("$[0].recognizedLiability")
                                .value(500000)
                );
    }

    @Test
    void getMyLiability_ShouldReturnLiability() throws Exception {

        LiabilityResponse response =
                new LiabilityResponse();

        response.setId(1L);
        response.setLiabilityName("Vehicle Loan");
        response.setLiabilityType(
                LiabilityType.VEHICLE_LOAN
        );
        response.setOutstandingAmount(
                new BigDecimal("800000")
        );
        response.setResponsibilityType(
                ResponsibilityType.INDIVIDUAL
        );
        response.setResponsibilityPercentage(
                new BigDecimal("100")
        );
        response.setRecognizedLiability(
                new BigDecimal("800000.00")
        );
        response.setIncludedInNetWorth(true);

        when(liabilityService.getMyLiability(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v2/liabilities/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(
                        jsonPath("$.liabilityName")
                                .value("Vehicle Loan")
                )
                .andExpect(
                        jsonPath("$.liabilityType")
                                .value("VEHICLE_LOAN")
                )
                .andExpect(
                        jsonPath("$.recognizedLiability")
                                .value(800000)
                )
                .andExpect(
                        jsonPath("$.includedInNetWorth")
                                .value(true)
                );
    }

    @Test
    void createLiability_ShouldRejectInvalidRequest() throws Exception {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setLiabilityName("");
        request.setLiabilityType(null);
        request.setOutstandingAmount(null);
        request.setResponsibilityType(null);
        request.setResponsibilityPercentage(null);
        request.setValuationDate(null);

        mockMvc.perform(
                        post("/api/v2/liabilities")
                                .contentType(APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isBadRequest());
    }
}