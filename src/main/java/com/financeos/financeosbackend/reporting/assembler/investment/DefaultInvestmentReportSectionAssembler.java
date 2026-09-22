package com.financeos.financeosbackend.reporting.assembler.investment;

import com.financeos.financeosbackend.reporting.collector.investment.ReportInvestmentData;
import com.financeos.financeosbackend.reporting.collector.investment.ReportInvestmentHistoricalData;
import com.financeos.financeosbackend.reporting.collector.investment.ReportInvestmentHoldingData;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentAllocationV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentHistoricalPerformanceV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportInvestmentHoldingV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionMetadata;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class DefaultInvestmentReportSectionAssembler
        implements InvestmentReportSectionAssembler {

    @Override
    public InvestmentReportV2Response assemble(
            ReportInvestmentData data) {

        if (data == null) {
            throw new IllegalArgumentException(
                    "Investment report data must not be null"
            );
        }

        InvestmentReportV2Response response =
                new InvestmentReportV2Response();

        response.setInvestedAmount(
                data.getInvestedAmount()
        );

        response.setPortfolioValue(
                data.getPortfolioValue()
        );

        response.setProfitLoss(
                data.getProfitLoss()
        );

        response.setReturnPercentage(
                data.getReturnPercentage()
        );

        response.setAssetAllocation(
                assembleAllocation(data)
        );

        /*
         * Assemble the complete holdings list.
         *
         * This list represents all investment holdings and is used
         * for immutable report snapshot persistence.
         */
        List<ReportInvestmentHoldingV2Response> holdings =
                assembleHoldings(data);

        response.setHoldings(holdings);

        /*
         * Best and worst performers are presentation subsets.
         *
         * Holdings with no invested capital are excluded because
         * a zero-investment holding has no meaningful performance.
         */
        response.setBestPerformers(
                getBestPerformers(holdings)
        );

        response.setWorstPerformers(
                getWorstPerformers(holdings)
        );

        response.setHistoricalPerformance(
                assembleHistoricalPerformance(data)
        );

        if (data.getValuationDate() != null) {
            response.setValuationDate(
                    data.getValuationDate().toString()
            );
        }

        response.setMetadata(
                buildMetadata(data)
        );

        return response;
    }

    private List<InvestmentAllocationV2Response> assembleAllocation(
            ReportInvestmentData data) {

        List<InvestmentAllocationV2Response> result =
                new ArrayList<>();

        Map<String, BigDecimal> allocation =
                data.getAssetAllocation();

        if (allocation == null || allocation.isEmpty()) {
            return result;
        }

        BigDecimal portfolioValue =
                data.getPortfolioValue();

        for (Map.Entry<String, BigDecimal> entry
                : allocation.entrySet()) {

            InvestmentAllocationV2Response item =
                    new InvestmentAllocationV2Response();

            item.setAssetType(
                    entry.getKey()
            );

            item.setAmount(
                    entry.getValue()
            );

            BigDecimal percentage =
                    BigDecimal.ZERO;

            if (portfolioValue != null
                    && portfolioValue.compareTo(BigDecimal.ZERO) > 0) {

                percentage = entry.getValue()
                        .divide(
                                portfolioValue,
                                6,
                                RoundingMode.HALF_UP
                        )
                        .multiply(
                                BigDecimal.valueOf(100)
                        );
            }

            item.setPercentage(
                    percentage
            );

            result.add(item);
        }

        result.sort(
                Comparator.comparing(
                        InvestmentAllocationV2Response::getAmount,
                        Comparator.reverseOrder()
                )
        );

        return result;
    }

    private List<ReportInvestmentHoldingV2Response> assembleHoldings(
            ReportInvestmentData data) {

        List<ReportInvestmentHoldingV2Response> result =
                new ArrayList<>();

        if (data.getHoldings() == null) {
            return result;
        }

        for (ReportInvestmentHoldingData holding
                : data.getHoldings()) {

            if (holding == null) {
                continue;
            }

            ReportInvestmentHoldingV2Response response =
                    new ReportInvestmentHoldingV2Response();

            response.setInvestmentId(
                    holding.getInvestmentId()
            );

            response.setInvestmentName(
                    holding.getInvestmentName()
            );

            response.setInvestmentType(
                    holding.getInvestmentType()
            );

            response.setInvestedAmount(
                    holding.getInvestedAmount()
            );

            response.setCurrentValue(
                    holding.getCurrentValue()
            );

            response.setProfitLoss(
                    holding.getProfitLoss()
            );

            response.setReturnPercentage(
                    holding.getReturnPercentage()
            );

            result.add(response);
        }

        return result;
    }

    private List<ReportInvestmentHoldingV2Response> getBestPerformers(
            List<ReportInvestmentHoldingV2Response> holdings) {

        return holdings.stream()
                .filter(this::hasInvestedCapital)
                .sorted(
                        Comparator.comparing(
                                ReportInvestmentHoldingV2Response
                                        ::getReturnPercentage,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
                .limit(5)
                .toList();
    }

    private List<ReportInvestmentHoldingV2Response> getWorstPerformers(
            List<ReportInvestmentHoldingV2Response> holdings) {

        return holdings.stream()
                .filter(this::hasInvestedCapital)
                .sorted(
                        Comparator.comparing(
                                ReportInvestmentHoldingV2Response
                                        ::getReturnPercentage,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
                .limit(5)
                .toList();
    }

    private boolean hasInvestedCapital(
            ReportInvestmentHoldingV2Response holding) {

        return holding != null
                && holding.getInvestedAmount() != null
                && holding.getInvestedAmount()
                .compareTo(BigDecimal.ZERO) > 0;
    }

    private List<InvestmentHistoricalPerformanceV2Response>
    assembleHistoricalPerformance(
            ReportInvestmentData data) {

        List<InvestmentHistoricalPerformanceV2Response> result =
                new ArrayList<>();

        if (data.getHistoricalPerformance() == null) {
            return result;
        }

        for (ReportInvestmentHistoricalData historical
                : data.getHistoricalPerformance()) {

            if (historical == null) {
                continue;
            }

            InvestmentHistoricalPerformanceV2Response response =
                    new InvestmentHistoricalPerformanceV2Response();

            if (historical.getValuationDate() != null) {
                response.setValuationDate(
                        historical.getValuationDate().toString()
                );
            }

            response.setInvestedAmount(
                    historical.getInvestedAmount()
            );

            response.setPortfolioValue(
                    historical.getPortfolioValue()
            );

            response.setProfitLoss(
                    historical.getProfitLoss()
            );

            response.setReturnPercentage(
                    historical.getReturnPercentage()
            );

            result.add(response);
        }

        result.sort(
                Comparator.comparing(
                        InvestmentHistoricalPerformanceV2Response
                                ::getValuationDate,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );

        return result;
    }

    private ReportSectionMetadata buildMetadata(
            ReportInvestmentData data) {

        boolean hasCurrentData =
                data.getInvestedAmount() != null
                        && data.getInvestedAmount()
                        .compareTo(BigDecimal.ZERO) > 0;

        if (!hasCurrentData) {
            return new ReportSectionMetadata(
                    ReportSectionStatus.NO_DATA,
                    "No investment data was available for the selected report."
            );
        }

        if (!data.isHistoricalDataAvailable()) {
            return new ReportSectionMetadata(
                    ReportSectionStatus.HISTORICAL_DATA_UNAVAILABLE,
                    "Current investment data is available, but historical valuation data was not available for the selected period."
            );
        }

        return new ReportSectionMetadata(
                ReportSectionStatus.AVAILABLE,
                "Investment data and historical valuation data are available."
        );
    }
}
