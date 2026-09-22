package com.financeos.financeosbackend.reporting.insight;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportChangeInsightService {

    private final ReportChangeAnalysisService changeAnalysisService;
    private final ReportExpenseContributorCollector contributorCollector;
    private final ReportChangeInsightAssembler insightAssembler;

    public ReportChangeInsightService(
            ReportChangeAnalysisService changeAnalysisService,
            ReportExpenseContributorCollector contributorCollector,
            ReportChangeInsightAssembler insightAssembler
    ) {
        this.changeAnalysisService = changeAnalysisService;
        this.contributorCollector = contributorCollector;
        this.insightAssembler = insightAssembler;
    }

    public List<ReportChangeInsightV2Response> generateInsights(
            User user,
            ReportPeriodResponse currentPeriod,
            ReportPeriodResponse previousPeriod,
            BigDecimal currentIncome,
            BigDecimal previousIncome,
            BigDecimal currentExpenses,
            BigDecimal previousExpenses,
            BigDecimal currentSavings,
            BigDecimal previousSavings
    ) {
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        if (currentPeriod == null || previousPeriod == null) {
            throw new IllegalArgumentException(
                    "Reporting periods are required"
            );
        }

        List<ReportChangeInsightV2Response> result =
                new ArrayList<>();

        ReportChangeData incomeChange =
                changeAnalysisService.analyzeMetric(
                        "Income",
                        currentIncome,
                        previousIncome
                );

        result.add(
                insightAssembler.assemble(incomeChange)
        );

        ReportChangeData expenseChange =
                changeAnalysisService.analyzeMetric(
                        "Expenses",
                        currentExpenses,
                        previousExpenses
                );

        expenseChange.setContributors(
                contributorCollector.collect(
                        user,
                        currentPeriod,
                        previousPeriod
                )
        );

        result.add(
                insightAssembler.assemble(expenseChange)
        );

        ReportChangeData savingsChange =
                changeAnalysisService.analyzeMetric(
                        "Savings",
                        currentSavings,
                        previousSavings
                );

        result.add(
                insightAssembler.assemble(savingsChange)
        );

        return result;
    }
}