package com.financeos.financeosbackend.reporting.insight;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportChangeInsightServiceTest {

    @Mock
    private ReportChangeAnalysisService changeAnalysisService;

    @Mock
    private ReportExpenseContributorCollector contributorCollector;

    @Mock
    private ReportChangeInsightAssembler insightAssembler;

    @InjectMocks
    private ReportChangeInsightService service;

    @Test
    void shouldGenerateIncomeExpenseAndSavingsInsights() {

        User user = new User();

        ReportPeriodResponse current =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        ReportPeriodResponse previous =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31)
                );

        ReportChangeData income =
                createChange("Income");

        ReportChangeData expenses =
                createChange("Expenses");

        ReportChangeData savings =
                createChange("Savings");

        ReportChangeInsightV2Response incomeResponse =
                new ReportChangeInsightV2Response();

        ReportChangeInsightV2Response expenseResponse =
                new ReportChangeInsightV2Response();

        ReportChangeInsightV2Response savingsResponse =
                new ReportChangeInsightV2Response();

        when(
                changeAnalysisService.analyzeMetric(
                        eq("Income"),
                        any(),
                        any()
                )
        ).thenReturn(income);

        when(
                changeAnalysisService.analyzeMetric(
                        eq("Expenses"),
                        any(),
                        any()
                )
        ).thenReturn(expenses);

        when(
                changeAnalysisService.analyzeMetric(
                        eq("Savings"),
                        any(),
                        any()
                )
        ).thenReturn(savings);

        when(
                contributorCollector.collect(
                        eq(user),
                        eq(current),
                        eq(previous)
                )
        ).thenReturn(List.of());

        when(insightAssembler.assemble(income))
                .thenReturn(incomeResponse);

        when(insightAssembler.assemble(expenses))
                .thenReturn(expenseResponse);

        when(insightAssembler.assemble(savings))
                .thenReturn(savingsResponse);

        List<ReportChangeInsightV2Response> result =
                service.generateInsights(
                        user,
                        current,
                        previous,
                        new BigDecimal("60000"),
                        new BigDecimal("55000"),
                        new BigDecimal("35000"),
                        new BigDecimal("30000"),
                        new BigDecimal("25000"),
                        new BigDecimal("25000")
                );

        assertEquals(3, result.size());
        assertEquals(incomeResponse, result.get(0));
        assertEquals(expenseResponse, result.get(1));
        assertEquals(savingsResponse, result.get(2));
    }

    private ReportChangeData createChange(String metric) {

        ReportChangeData data =
                new ReportChangeData();

        data.setMetric(metric);
        data.setCurrentValue(new BigDecimal("100"));
        data.setPreviousValue(new BigDecimal("90"));
        data.setAbsoluteChange(new BigDecimal("10"));
        data.setPercentageChange(new BigDecimal("11.11"));
        data.setDirection("INCREASED");

        return data;
    }
}