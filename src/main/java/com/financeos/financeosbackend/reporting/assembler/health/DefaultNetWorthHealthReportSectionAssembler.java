package com.financeos.financeosbackend.reporting.assembler.health;

import com.financeos.financeosbackend.reporting.collector.health.ReportFinancialHealthData;
import com.financeos.financeosbackend.reporting.collector.health.ReportNetWorthData;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialHealthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionMetadata;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultNetWorthHealthReportSectionAssembler
        implements NetWorthHealthReportSectionAssembler {

    @Override
    public NetWorthReportV2Response assembleNetWorth(
            ReportNetWorthData data
    ) {
        NetWorthReportV2Response response =
                new NetWorthReportV2Response();

        if (data == null) {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "Net worth data is unavailable."
                    )
            );
            return response;
        }

        response.setRecognizedAssets(
                data.getRecognizedAssets()
        );

        response.setRecognizedLiabilities(
                data.getRecognizedLiabilities()
        );

        response.setNetWorth(
                data.getNetWorth()
        );

        if (!data.isHistoricalDataAvailable()) {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.HISTORICAL_DATA_UNAVAILABLE,
                            "Net worth is based on the current recognized financial position; historical period-end data is unavailable."
                    )
            );
        } else if (isZeroOrNull(data.getNetWorth())
                && isZeroOrNull(data.getRecognizedAssets())
                && isZeroOrNull(data.getRecognizedLiabilities())) {

            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "No recognized net worth data is available."
                    )
            );
        } else {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.AVAILABLE,
                            "Historical net worth data is available."
                    )
            );
        }

        return response;
    }

    @Override
    public FinancialHealthReportV2Response assembleFinancialHealth(
            ReportFinancialHealthData data
    ) {
        FinancialHealthReportV2Response response =
                new FinancialHealthReportV2Response();

        if (data == null) {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "Financial health data is unavailable."
                    )
            );
            return response;
        }

        response.setCashFlowHealth(
                data.getCashFlowHealth()
        );

        response.setDebtHealth(
                data.getDebtHealth()
        );

        response.setSavingsHealth(
                data.getSavingsHealth()
        );

        response.setInvestmentHealth(
                data.getInvestmentHealth()
        );

        response.setGoalHealth(
                data.getGoalHealth()
        );

        response.setWealthHealth(
                data.getWealthHealth()
        );

        response.setOverallStatus(
                data.getOverallStatus()
        );

        if (!data.isHistoricalDataAvailable()) {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.HISTORICAL_DATA_UNAVAILABLE,
                            "Financial health reflects the current financial state; historical health data is unavailable."
                    )
            );
        } else {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.AVAILABLE,
                            "Historical financial health data is available."
                    )
            );
        }

        return response;
    }

    private boolean isZeroOrNull(BigDecimal value) {
        return value == null
                || value.compareTo(BigDecimal.ZERO) == 0;
    }
}