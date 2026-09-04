package com.financeos.financeosbackend.financialposition.service;

import com.financeos.financeosbackend.networth.service.NetWorthService;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import java.math.BigDecimal;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.asset.enums.AssetType;
import java.util.Map;
import com.financeos.financeosbackend.common.service.CurrentUserService;



@Service
public class FinancialPositionService {

    private final NetWorthService netWorthService;
    private final CashFlowService cashFlowService;
    private final InvestmentRepository investmentRepository;
    private final CurrentUserService currentUserService;

    public FinancialPositionService(
            NetWorthService netWorthService,
            CashFlowService cashFlowService,
            InvestmentRepository investmentRepository,
            CurrentUserService currentUserService
    ) {
        this.netWorthService = netWorthService;
        this.cashFlowService = cashFlowService;
        this.investmentRepository = investmentRepository;
        this.currentUserService = currentUserService;
    }

    public BigDecimal calculateNetWorth() {
        return netWorthService.calculateNetWorth();
    }

    public BigDecimal calculateNetCashFlow() {
        return cashFlowService.calculateNetCashFlow();
    }

    public BigDecimal calculateInvestmentValue() {
        User user = currentUserService.getCurrentUser();
        return investmentRepository.getTotalCurrentValueByUser(user);
    }

    public BigDecimal calculateDebtValue() {
        return netWorthService.calculateRecognizedLiabilities();
    }

    public BigDecimal calculateLiquidAssets() {
        return netWorthService.calculateRecognizedLiquidAssets();
    }

    public Map<AssetType, BigDecimal> calculateAssetAllocation() {
        return netWorthService.calculateRecognizedAssetAllocation();
    }

    public BigDecimal calculateSavings() {
        return cashFlowService.calculateSavings();
    }

    public BigDecimal calculateSavingsRate() {
        return cashFlowService.calculateSavingsRate();
    }


}