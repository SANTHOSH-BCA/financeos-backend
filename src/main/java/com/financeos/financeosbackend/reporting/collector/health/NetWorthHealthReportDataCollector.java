package com.financeos.financeosbackend.reporting.collector.health;

import com.financeos.financeosbackend.financialhealth.service.FinancialHealthService;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class NetWorthHealthReportDataCollector {

    private final NetWorthService netWorthService;
    private final FinancialHealthService financialHealthService;

    public NetWorthHealthReportDataCollector(
            NetWorthService netWorthService,
            FinancialHealthService financialHealthService
    ) {
        this.netWorthService = netWorthService;
        this.financialHealthService = financialHealthService;
    }

    public ReportNetWorthData collectNetWorth(
            User user,
            ReportPeriodResponse period
    ) {
        validate(user, period);

        ReportNetWorthData data = new ReportNetWorthData();

        data.setRecognizedAssets(
                netWorthService.calculateRecognizedAssets()
        );

        data.setRecognizedLiabilities(
                netWorthService.calculateRecognizedLiabilities()
        );

        data.setNetWorth(
                netWorthService.calculateNetWorth()
        );

        /*
         * NetWorthService currently exposes current-state data only.
         * Therefore we must not represent it as historical period-end data.
         */
        data.setHistoricalDataAvailable(false);

        return data;
    }

    public ReportFinancialHealthData collectFinancialHealth(
            User user,
            ReportPeriodResponse period
    ) {
        validate(user, period);

        ReportFinancialHealthData data =
                new ReportFinancialHealthData();

        data.setCashFlowHealth(
                financialHealthService
                        .calculateCashFlowHealth()
                        .getStatus()
        );

        data.setDebtHealth(
                financialHealthService
                        .calculateDebtHealth()
                        .getStatus()
        );

        data.setSavingsHealth(
                financialHealthService
                        .calculateSavingsHealth()
                        .getStatus()
        );

        data.setInvestmentHealth(
                financialHealthService
                        .calculateInvestmentHealth()
                        .getStatus()
        );

        data.setGoalHealth(
                financialHealthService
                        .calculateGoalHealth()
                        .getStatus()
        );

        data.setWealthHealth(
                financialHealthService
                        .calculateWealthHealth()
                        .getStatus()
        );

        data.setOverallStatus(
                financialHealthService
                        .calculateOverallFinancialHealth()
                        .getOverallStatus()
        );

        /*
         * FinancialHealthService currently evaluates current state.
         * It cannot reconstruct historical health for an old report period.
         */
        data.setHistoricalDataAvailable(false);

        return data;
    }

    private void validate(
            User user,
            ReportPeriodResponse period
    ) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null.");
        }

        if (period == null) {
            throw new IllegalArgumentException("Report period must not be null.");
        }

        if (period.getStartDate() == null
                || period.getEndDate() == null) {
            throw new IllegalArgumentException(
                    "Report period dates must not be null."
            );
        }
    }
}