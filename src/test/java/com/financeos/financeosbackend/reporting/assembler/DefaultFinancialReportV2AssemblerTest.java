package com.financeos.financeosbackend.reporting.assembler;

import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.AssetReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.CashFlowReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ExpenseReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialHealthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.GoalReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DefaultFinancialReportV2AssemblerTest {

    private final DefaultFinancialReportV2Assembler assembler =
            new DefaultFinancialReportV2Assembler();

    @Test
    void shouldAssembleCompleteFinancialReport() {

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        IncomeReportV2Response income =
                new IncomeReportV2Response();

        ExpenseReportV2Response expenses =
                new ExpenseReportV2Response();

        CashFlowReportV2Response cashFlow =
                new CashFlowReportV2Response();

        InvestmentReportV2Response investments =
                new InvestmentReportV2Response();

        GoalReportV2Response goals =
                new GoalReportV2Response();

        AssetReportV2Response assets =
                new AssetReportV2Response();

        LiabilityReportV2Response liabilities =
                new LiabilityReportV2Response();

        NetWorthReportV2Response netWorth =
                new NetWorthReportV2Response();

        FinancialHealthReportV2Response health =
                new FinancialHealthReportV2Response();

        ReportComparisonV2Response comparison =
                new ReportComparisonV2Response();

        comparison.setComparisonAvailable(true);

        FinancialReportV2Response result =
                assembler.assemble(
                        period,
                        income,
                        expenses,
                        cashFlow,
                        investments,
                        goals,
                        assets,
                        liabilities,
                        netWorth,
                        health,
                        comparison,
                        null
                );

        assertNotNull(result);

        assertNull(result.getReportId());

        assertSame(period, result.getReportPeriod());

        assertNotNull(result.getGeneratedAt());

        assertTrue(
                result.getGeneratedAt()
                        .isBefore(
                                LocalDateTime.now().plusSeconds(1)
                        )
        );

        assertEquals(
                "PREVIEW",
                result.getReportStatus()
        );

        assertSame(income, result.getIncome());
        assertSame(expenses, result.getExpenses());
        assertSame(cashFlow, result.getCashFlow());
        assertSame(investments, result.getInvestments());
        assertSame(goals, result.getGoals());
        assertSame(assets, result.getAssets());
        assertSame(liabilities, result.getLiabilities());
        assertSame(netWorth, result.getNetWorth());
        assertSame(health, result.getFinancialHealth());
        assertSame(comparison, result.getComparison());
    }

    @Test
    void shouldRejectNullPeriod() {

        assertThrows(
                IllegalArgumentException.class,
                () -> assembler.assemble(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );
    }

    @Test
    void shouldAllowUnavailableSectionsForPartialReport() {

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        IncomeReportV2Response income =
                new IncomeReportV2Response();

        FinancialReportV2Response result =
                assembler.assemble(
                        period,
                        income,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        assertNotNull(result);

        assertSame(
                income,
                result.getIncome()
        );

        assertNull(
                result.getExpenses()
        );

        assertNull(
                result.getInvestments()
        );

        assertNull(
                result.getComparison()
        );

        assertNull(
                result.getChanges()
        );
    }
}
