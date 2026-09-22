package com.financeos.financeosbackend.reporting.insight;

import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeContributorV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultReportChangeInsightAssembler
        implements ReportChangeInsightAssembler {

    @Override
    public ReportChangeInsightV2Response assemble(
            ReportChangeData data
    ) {
        if (data == null) {
            return null;
        }

        ReportChangeInsightV2Response response =
                new ReportChangeInsightV2Response();

        response.setMetric(data.getMetric());
        response.setCurrentValue(data.getCurrentValue());
        response.setPreviousValue(data.getPreviousValue());
        response.setAbsoluteChange(data.getAbsoluteChange());
        response.setPercentageChange(data.getPercentageChange());
        response.setDirection(data.getDirection());
        response.setSignificant(data.isSignificant());

        response.setWhatChanged(
                buildWhatChanged(data)
        );

        response.setWhyDidItChange(
                buildWhyDidItChange(data)
        );

        response.setContributors(
                mapContributors(data.getContributors())
        );

        return response;
    }

    private String buildWhatChanged(
            ReportChangeData data
    ) {
        BigDecimal change = data.getAbsoluteChange();

        if (change == null ||
                change.compareTo(BigDecimal.ZERO) == 0) {

            return data.getMetric() + " remained unchanged.";
        }

        String direction =
                "INCREASED".equals(data.getDirection())
                        ? "increased"
                        : "decreased";

        StringBuilder result = new StringBuilder();

        result.append(data.getMetric())
                .append(" ")
                .append(direction)
                .append(" by ")
                .append(formatAmount(change.abs()));

        if (data.getPercentageChange() != null) {
            result.append(" (")
                    .append(
                            formatPercentage(
                                    data.getPercentageChange().abs()
                            )
                    )
                    .append("%).");
        } else {
            result.append(" (percentage change unavailable).");
        }

        return result.toString();
    }

    private String buildWhyDidItChange(
            ReportChangeData data
    ) {
        List<ReportChangeContributorData> contributors =
                data.getContributors();

        if (contributors == null || contributors.isEmpty()) {
            return "The available report data does not identify a specific contributor.";
        }

        List<String> descriptions = new ArrayList<>();

        for (ReportChangeContributorData contributor :
                contributors) {

            if (contributor == null ||
                    contributor.getAbsoluteChange() == null ||
                    contributor.getAbsoluteChange()
                            .compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            String direction =
                    contributor.getAbsoluteChange()
                            .compareTo(BigDecimal.ZERO) > 0
                            ? "increased"
                            : "decreased";

            descriptions.add(
                    contributor.getName()
                            + " "
                            + direction
                            + " by "
                            + formatAmount(
                            contributor
                                    .getAbsoluteChange()
                                    .abs()
                    )
            );
        }

        if (descriptions.isEmpty()) {
            return "The available report data does not identify a specific contributor.";
        }

        return String.join(", ", descriptions) + ".";
    }

    private List<ReportChangeContributorV2Response> mapContributors(
            List<ReportChangeContributorData> contributors
    ) {
        List<ReportChangeContributorV2Response> result =
                new ArrayList<>();

        if (contributors == null) {
            return result;
        }

        for (ReportChangeContributorData contributor :
                contributors) {

            if (contributor == null) {
                continue;
            }

            ReportChangeContributorV2Response response =
                    new ReportChangeContributorV2Response();

            response.setName(contributor.getName());
            response.setCurrentValue(
                    contributor.getCurrentValue()
            );
            response.setPreviousValue(
                    contributor.getPreviousValue()
            );
            response.setAbsoluteChange(
                    contributor.getAbsoluteChange()
            );
            response.setPercentageChange(
                    contributor.getPercentageChange()
            );

            result.add(response);
        }

        return result;
    }

    private String formatAmount(BigDecimal value) {
        if (value == null) {
            return "0.00";
        }

        return value.setScale(
                2,
                java.math.RoundingMode.HALF_UP
        ).toPlainString();
    }

    private String formatPercentage(BigDecimal value) {
        if (value == null) {
            return "0.00";
        }

        return value.setScale(
                2,
                java.math.RoundingMode.HALF_UP
        ).toPlainString();
    }
}
