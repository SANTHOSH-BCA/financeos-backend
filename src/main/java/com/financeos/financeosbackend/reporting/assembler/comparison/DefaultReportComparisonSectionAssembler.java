package com.financeos.financeosbackend.reporting.assembler.comparison;

import com.financeos.financeosbackend.reporting.comparison.ReportComparisonData;
import com.financeos.financeosbackend.reporting.comparison.ReportMetricChangeData;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportMetricChangeV2Response;
import org.springframework.stereotype.Service;

@Service
public class DefaultReportComparisonSectionAssembler
        implements ReportComparisonSectionAssembler {

    @Override
    public ReportComparisonV2Response assemble(
            ReportComparisonData data
    ) {
        ReportComparisonV2Response response =
                new ReportComparisonV2Response();

        if (data == null) {
            response.setComparisonAvailable(false);
            return response;
        }

        response.setComparisonAvailable(
                data.isComparisonAvailable()
        );

        response.setCurrentPeriod(
                data.getCurrentPeriod()
        );

        response.setPreviousPeriod(
                data.getPreviousPeriod()
        );

        response.setIncome(
                mapMetric(data.getIncome())
        );

        response.setExpenses(
                mapMetric(data.getExpenses())
        );

        response.setSavings(
                mapMetric(data.getSavings())
        );

        response.setInvestments(
                mapMetric(data.getInvestments())
        );

        response.setNetWorth(
                mapMetric(data.getNetWorth())
        );

        return response;
    }

    private ReportMetricChangeV2Response mapMetric(
            ReportMetricChangeData data
    ) {
        if (data == null) {
            return null;
        }

        ReportMetricChangeV2Response response =
                new ReportMetricChangeV2Response();

        response.setCurrentValue(
                data.getCurrentValue()
        );

        response.setPreviousValue(
                data.getPreviousValue()
        );

        response.setAbsoluteChange(
                data.getAbsoluteChange()
        );

        response.setPercentageChange(
                data.getPercentageChange()
        );

        return response;
    }
}