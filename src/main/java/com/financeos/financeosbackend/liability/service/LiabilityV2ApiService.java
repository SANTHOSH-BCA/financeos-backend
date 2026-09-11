package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.dto.DebtFinancialFutureResponse;
import com.financeos.financeosbackend.liability.dto.DebtFinancialHealthResponse;
import com.financeos.financeosbackend.liability.dto.DebtInvestmentTradeoffResponse;
import com.financeos.financeosbackend.liability.dto.DebtNetWorthImpactResponse;
import com.financeos.financeosbackend.liability.dto.LiabilityV2ApiResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LiabilityV2ApiService {

    private final DebtBurdenService debtBurdenService;
    private final DebtInvestmentTradeoffService debtInvestmentTradeoffService;
    private final DebtNetWorthImpactService debtNetWorthImpactService;
    private final DebtFinancialHealthService debtFinancialHealthService;
    private final DebtFinancialFutureService debtFinancialFutureService;

    public LiabilityV2ApiService(
            DebtBurdenService debtBurdenService,
            DebtInvestmentTradeoffService debtInvestmentTradeoffService,
            DebtNetWorthImpactService debtNetWorthImpactService,
            DebtFinancialHealthService debtFinancialHealthService,
            DebtFinancialFutureService debtFinancialFutureService
    ) {
        this.debtBurdenService = debtBurdenService;
        this.debtInvestmentTradeoffService = debtInvestmentTradeoffService;
        this.debtNetWorthImpactService = debtNetWorthImpactService;
        this.debtFinancialHealthService = debtFinancialHealthService;
        this.debtFinancialFutureService = debtFinancialFutureService;
    }

    public LiabilityV2ApiResponse getOverview() {

        DebtBurdenResponse burden =
                debtBurdenService.getMyDebtBurden();

        DebtInvestmentTradeoffResponse investmentTradeoff =
                debtInvestmentTradeoffService.getTradeoff();

        DebtNetWorthImpactResponse netWorthImpact =
                debtNetWorthImpactService.getImpact();

        DebtFinancialHealthResponse financialHealth =
                debtFinancialHealthService.getDebtFinancialHealth();

        DebtFinancialFutureResponse financialFuture =
                debtFinancialFutureService.getDebtFinancialFuture();

        return new LiabilityV2ApiResponse(
                normalize(burden.getTotalOutstanding()),
                normalize(burden.getTotalMonthlyPayment()),
                normalize(investmentTradeoff.getTotalInvestmentValue()),
                normalize(netWorthImpact.getNetWorth()),
                burden.getDebtPaymentRatio(),
                investmentTradeoff.getDebtToInvestmentRatio(),
                burden.getActiveLiabilityCount(),
                burden.getBurdenLevel(),
                financialHealth.getDebtHealth(),
                financialFuture.getProjectionStatus()
        );
    }

    private BigDecimal normalize(BigDecimal value) {

        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return value.setScale(2, RoundingMode.HALF_UP);
    }
}