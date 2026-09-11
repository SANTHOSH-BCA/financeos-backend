package com.financeos.financeosbackend.liability.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.liability.dto.CreateLiabilityRepaymentRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityRepaymentResponse;
import com.financeos.financeosbackend.liability.service.LiabilityRepaymentService;
import com.financeos.financeosbackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LiabilityRepaymentController.class)
class LiabilityRepaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LiabilityRepaymentService repaymentService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void shouldCreateRepayment() throws Exception {

        CreateLiabilityRepaymentRequest request =
                new CreateLiabilityRepaymentRequest();

        request.setPaymentAmount(new BigDecimal("25000"));
        request.setPrincipalAmount(new BigDecimal("18000"));
        request.setInterestAmount(new BigDecimal("7000"));
        request.setRepaymentDate(LocalDate.of(2026, 9, 1));
        request.setNotes("September EMI");

        LiabilityRepaymentResponse response =
                new LiabilityRepaymentResponse();

        response.setId(1L);
        response.setLiabilityId(10L);
        response.setPaymentAmount(new BigDecimal("25000"));
        response.setPrincipalAmount(new BigDecimal("18000"));
        response.setInterestAmount(new BigDecimal("7000"));
        response.setRepaymentDate(LocalDate.of(2026, 9, 1));

        when(repaymentService.createRepayment(
                any(Long.class),
                any(CreateLiabilityRepaymentRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        post("/api/v2/liabilities/10/repayments")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.liabilityId").value(10))
                .andExpect(jsonPath("$.paymentAmount").value(25000))
                .andExpect(jsonPath("$.principalAmount").value(18000));
    }

    @Test
    @WithMockUser
    void shouldGetRepayments() throws Exception {

        LiabilityRepaymentResponse response =
                new LiabilityRepaymentResponse();

        response.setId(1L);
        response.setLiabilityId(10L);
        response.setPaymentAmount(new BigDecimal("25000"));

        when(repaymentService.getRepayments(10L))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v2/liabilities/10/repayments")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].liabilityId").value(10))
                .andExpect(jsonPath("$[0].paymentAmount").value(25000));
    }

    @Test
    @WithMockUser
    void shouldGetSingleRepayment() throws Exception {

        LiabilityRepaymentResponse response =
                new LiabilityRepaymentResponse();

        response.setId(5L);
        response.setLiabilityId(10L);
        response.setPaymentAmount(new BigDecimal("15000"));

        when(repaymentService.getRepayment(10L, 5L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v2/liabilities/10/repayments/5")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.liabilityId").value(10))
                .andExpect(jsonPath("$.paymentAmount").value(15000));
    }
}