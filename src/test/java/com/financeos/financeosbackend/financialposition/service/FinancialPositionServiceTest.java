package com.financeos.financeosbackend.financialposition.service;

import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialPositionServiceTest {

    @Mock
    private NetWorthService netWorthService;

    @Mock
    private CashFlowService cashFlowService;

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private CurrentUserService currentUserService;

    private FinancialPositionService financialPositionService;

    private User user;

    @BeforeEach
    void setUp() {

        financialPositionService =
                new FinancialPositionService(
                        netWorthService,
                        cashFlowService,
                        investmentRepository,
                        currentUserService
                );

        user = new User();
        user.setId(1L);
    }

    @Test
    void calculateNetWorth_ShouldReturnNetWorthFromNetWorthService() {

        when(netWorthService.calculateNetWorth())
                .thenReturn(new BigDecimal("350000.00"));

        BigDecimal result =
                financialPositionService.calculateNetWorth();

        assertEquals(
                0,
                new BigDecimal("350000.00").compareTo(result)
        );
    }

    @Test
    void calculateNetCashFlow_ShouldReturnNetCashFlowFromCashFlowService() {

        when(cashFlowService.calculateNetCashFlow())
                .thenReturn(new BigDecimal("47200.00"));

        BigDecimal result =
                financialPositionService.calculateNetCashFlow();

        assertEquals(
                0,
                new BigDecimal("47200.00").compareTo(result)
        );
    }

    @Test
    void calculateInvestmentValue_ShouldReturnCurrentInvestmentValue() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalCurrentValueByUser(user))
                .thenReturn(new BigDecimal("250000.00"));

        BigDecimal result =
                financialPositionService.calculateInvestmentValue();

        assertEquals(
                0,
                new BigDecimal("250000.00").compareTo(result)
        );
    }

    @Test
    void calculateDebtValue_ShouldReturnRecognizedLiabilities() {

        when(netWorthService.calculateRecognizedLiabilities())
                .thenReturn(new BigDecimal("1250000.00"));

        BigDecimal result =
                financialPositionService.calculateDebtValue();

        assertEquals(
                0,
                new BigDecimal("1250000.00").compareTo(result)
        );
    }

    @Test
    void calculateLiquidAssets_ShouldReturnRecognizedLiquidAssets() {

        when(netWorthService.calculateRecognizedLiquidAssets())
                .thenReturn(new BigDecimal("60000.00"));

        BigDecimal result =
                financialPositionService.calculateLiquidAssets();

        assertEquals(
                0,
                new BigDecimal("60000.00").compareTo(result)
        );
    }

    @Test
    void calculateAssetAllocation_ShouldReturnRecognizedAssetAllocation() {

        Map<AssetType, BigDecimal> allocation =
                new EnumMap<>(AssetType.class);

        allocation.put(
                AssetType.CASH,
                new BigDecimal("10000.00")
        );

        allocation.put(
                AssetType.BANK_ACCOUNT,
                new BigDecimal("50000.00")
        );

        allocation.put(
                AssetType.GOLD,
                new BigDecimal("25000.00")
        );

        when(netWorthService.calculateRecognizedAssetAllocation())
                .thenReturn(allocation);

        Map<AssetType, BigDecimal> result =
                financialPositionService.calculateAssetAllocation();

        assertEquals(allocation, result);
    }

    @Test
    void calculateSavings_ShouldReturnSavingsFromCashFlowService() {

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("47200.00"));

        BigDecimal result =
                financialPositionService.calculateSavings();

        assertEquals(
                0,
                new BigDecimal("47200.00").compareTo(result)
        );
    }

    @Test
    void calculateSavingsRate_ShouldReturnSavingsRateFromCashFlowService() {

        when(cashFlowService.calculateSavingsRate())
                .thenReturn(new BigDecimal("85.82"));

        BigDecimal result =
                financialPositionService.calculateSavingsRate();

        assertEquals(
                0,
                new BigDecimal("85.82").compareTo(result)
        );
    }
}