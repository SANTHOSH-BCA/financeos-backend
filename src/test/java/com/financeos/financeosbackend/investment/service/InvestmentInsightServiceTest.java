package com.financeos.financeosbackend.investment.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.investment.dto.InvestmentInsightResponse;
import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentInsightServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private InvestmentInsightService investmentInsightService;

    @Test
    void getInvestmentInsights_ShouldCalculateAssetClassConcentration() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Investment mutualFund1 = new Investment();
        mutualFund1.setInvestmentName("Mutual Fund 1");
        mutualFund1.setInvestmentType("Mutual Fund");
        mutualFund1.setTotalInvestedAmount(new BigDecimal("60000"));
        mutualFund1.setCurrentValue(new BigDecimal("65000"));

        Investment mutualFund2 = new Investment();
        mutualFund2.setInvestmentName("Mutual Fund 2");
        mutualFund2.setInvestmentType("Mutual Fund");
        mutualFund2.setTotalInvestedAmount(new BigDecimal("20000"));
        mutualFund2.setCurrentValue(new BigDecimal("22000"));

        Investment stocks = new Investment();
        stocks.setInvestmentName("Stocks");
        stocks.setInvestmentType("Stocks");
        stocks.setTotalInvestedAmount(new BigDecimal("20000"));
        stocks.setCurrentValue(new BigDecimal("21000"));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.findByUser(
                eq(user),
                any(Pageable.class)
        )).thenReturn(
                new PageImpl<>(
                        List.of(mutualFund1, mutualFund2, stocks)
                )
        );

        List<InvestmentInsightResponse> insights =
                investmentInsightService.getInvestmentInsights();

        assertNotNull(insights);

        InvestmentInsightResponse concentrationInsight =
                insights.stream()
                        .filter(insight ->
                                "ASSET_CLASS_CONCENTRATION"
                                        .equals(insight.getType())
                        )
                        .findFirst()
                        .orElse(null);

        assertNotNull(concentrationInsight);

        assertTrue(
                concentrationInsight.getMessage()
                        .contains("Mutual Fund")
        );

        assertTrue(
                concentrationInsight.getMessage()
                        .contains("80.00%")
        );

        verify(currentUserService).getCurrentUser();

        verify(investmentRepository).findByUser(
                eq(user),
                any(Pageable.class)
        );
    }
}