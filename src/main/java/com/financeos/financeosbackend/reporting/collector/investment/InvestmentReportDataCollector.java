package com.financeos.financeosbackend.reporting.collector.investment;

import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.entity.InvestmentValuationHistory;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentValuationHistoryRepository;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvestmentReportDataCollector {

    private final InvestmentRepository investmentRepository;
    private final InvestmentValuationHistoryRepository valuationHistoryRepository;

    public InvestmentReportDataCollector(
            InvestmentRepository investmentRepository,
            InvestmentValuationHistoryRepository valuationHistoryRepository) {

        this.investmentRepository = investmentRepository;
        this.valuationHistoryRepository = valuationHistoryRepository;
    }

    public ReportInvestmentData collect(
            User user,
            ReportPeriodResponse period) {

        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        if (period == null) {
            throw new IllegalArgumentException("Report period must not be null");
        }

        List<Investment> investments =
                investmentRepository.findByUser(
                        user,
                        Pageable.unpaged()
                ).getContent();

        ReportInvestmentData data = new ReportInvestmentData();

        if (investments.isEmpty()) {
            data.setAssetAllocation(new HashMap<>());
            data.setHistoricalDataAvailable(false);
            return data;
        }

        Map<String, BigDecimal> allocation = new HashMap<>();
        List<ReportInvestmentHoldingData> holdings = new ArrayList<>();

        BigDecimal totalInvested = BigDecimal.ZERO;
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Investment investment : investments) {

            BigDecimal investedAmount =
                    investment.getTotalInvestedAmount() != null
                            ? investment.getTotalInvestedAmount()
                            : investment.getAmount();

            if (investedAmount == null) {
                investedAmount = BigDecimal.ZERO;
            }

            BigDecimal currentValue =
                    investment.getCurrentValue() != null
                            ? investment.getCurrentValue()
                            : investedAmount;

            if (currentValue == null) {
                currentValue = BigDecimal.ZERO;
            }

            BigDecimal profitLoss =
                    currentValue.subtract(investedAmount);

            BigDecimal returnPercentage =
                    calculateReturn(investedAmount, profitLoss);

            ReportInvestmentHoldingData holding =
                    new ReportInvestmentHoldingData();

            holding.setInvestmentId(investment.getId());
            holding.setInvestmentName(investment.getInvestmentName());

            holding.setInvestmentType(
                    investment.getInvestmentType() != null
                            ? investment.getInvestmentType()
                            : null
            );

            holding.setInvestedAmount(investedAmount);
            holding.setCurrentValue(currentValue);
            holding.setProfitLoss(profitLoss);
            holding.setReturnPercentage(returnPercentage);

            holdings.add(holding);

            totalInvested = totalInvested.add(investedAmount);
            totalValue = totalValue.add(currentValue);

            String investmentType =
                    investment.getInvestmentType() != null
                            ? investment.getInvestmentType()
                            : "UNKNOWN";

            allocation.merge(
                    investmentType,
                    currentValue,
                    BigDecimal::add
            );
        }

        data.setInvestedAmount(totalInvested);
        data.setPortfolioValue(totalValue);

        BigDecimal portfolioProfitLoss =
                totalValue.subtract(totalInvested);

        data.setProfitLoss(portfolioProfitLoss);

        data.setReturnPercentage(
                calculateReturn(
                        totalInvested,
                        portfolioProfitLoss
                )
        );

        data.setAssetAllocation(allocation);
        data.setHoldings(holdings);

        collectHistoricalData(
                investments,
                period,
                data
        );

        return data;
    }

    private void collectHistoricalData(
            List<Investment> investments,
            ReportPeriodResponse period,
            ReportInvestmentData data) {

        List<ReportInvestmentHistoricalData> historicalData =
                new ArrayList<>();

        for (Investment investment : investments) {

            valuationHistoryRepository
                    .findTopByInvestmentAndValuationDateLessThanEqualOrderByValuationDateDesc(
                            investment,
                            period.getEndDate()
                    )
                    .ifPresent(history ->
                            historicalData.add(
                                    mapHistorical(history)
                            )
                    );
        }

        data.setHistoricalPerformance(historicalData);
        data.setHistoricalDataAvailable(!historicalData.isEmpty());

        if (!historicalData.isEmpty()) {
            data.setValuationDate(
                    historicalData.stream()
                            .map(ReportInvestmentHistoricalData::getValuationDate)
                            .max(java.time.LocalDate::compareTo)
                            .orElse(null)
            );
        }
    }

    private ReportInvestmentHistoricalData mapHistorical(
            InvestmentValuationHistory history) {

        ReportInvestmentHistoricalData data =
                new ReportInvestmentHistoricalData();

        data.setValuationDate(history.getValuationDate());

        BigDecimal invested =
                history.getInvestedAmount() != null
                        ? history.getInvestedAmount()
                        : BigDecimal.ZERO;

        BigDecimal value =
                history.getCurrentValue() != null
                        ? history.getCurrentValue()
                        : BigDecimal.ZERO;

        BigDecimal profitLoss =
                value.subtract(invested);

        data.setInvestedAmount(invested);
        data.setPortfolioValue(value);
        data.setProfitLoss(profitLoss);

        data.setReturnPercentage(
                calculateReturn(
                        invested,
                        profitLoss
                )
        );

        return data;
    }

    private BigDecimal calculateReturn(
            BigDecimal investedAmount,
            BigDecimal profitLoss) {

        if (investedAmount == null
                || investedAmount.compareTo(BigDecimal.ZERO) <= 0) {

            return BigDecimal.ZERO;
        }

        return profitLoss
                .divide(
                        investedAmount,
                        6,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100));
    }
}