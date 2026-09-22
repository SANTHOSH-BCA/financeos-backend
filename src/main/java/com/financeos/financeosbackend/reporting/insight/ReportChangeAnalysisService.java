package com.financeos.financeosbackend.reporting.insight;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Service
public class ReportChangeAnalysisService {

    private static final BigDecimal SIGNIFICANCE_THRESHOLD =
            new BigDecimal("5.00");

    private static final int MAX_CONTRIBUTORS = 5;

    public ReportChangeData analyzeMetric(
            String metric,
            BigDecimal currentValue,
            BigDecimal previousValue
    ) {
        BigDecimal current = normalize(currentValue);
        BigDecimal previous = normalize(previousValue);

        BigDecimal absoluteChange =
                current.subtract(previous)
                        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal percentageChange =
                calculatePercentageChange(
                        absoluteChange,
                        previous
                );

        ReportChangeData result =
                new ReportChangeData();

        result.setMetric(metric);
        result.setCurrentValue(current);
        result.setPreviousValue(previous);
        result.setAbsoluteChange(absoluteChange);
        result.setPercentageChange(percentageChange);

        if (absoluteChange.compareTo(BigDecimal.ZERO) > 0) {
            result.setDirection("INCREASED");
        } else if (absoluteChange.compareTo(BigDecimal.ZERO) < 0) {
            result.setDirection("DECREASED");
        } else {
            result.setDirection("UNCHANGED");
        }

        result.setSignificant(
                percentageChange != null
                        && percentageChange.abs()
                        .compareTo(SIGNIFICANCE_THRESHOLD) >= 0
        );

        return result;
    }

    public List<ReportChangeContributorData> identifyContributors(
            Map<String, BigDecimal> currentValues,
            Map<String, BigDecimal> previousValues
    ) {
        List<ReportChangeContributorData> contributors =
                new ArrayList<>();

        Set<String> names = new HashSet<>();

        if (currentValues != null) {
            names.addAll(currentValues.keySet());
        }

        if (previousValues != null) {
            names.addAll(previousValues.keySet());
        }

        for (String name : names) {

            BigDecimal current =
                    normalize(
                            currentValues == null
                                    ? null
                                    : currentValues.get(name)
                    );

            BigDecimal previous =
                    normalize(
                            previousValues == null
                                    ? null
                                    : previousValues.get(name)
                    );

            BigDecimal absoluteChange =
                    current.subtract(previous)
                            .setScale(2, RoundingMode.HALF_UP);

            if (absoluteChange.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            BigDecimal percentageChange =
                    calculatePercentageChange(
                            absoluteChange,
                            previous
                    );

            ReportChangeContributorData contributor =
                    new ReportChangeContributorData();

            contributor.setName(name);
            contributor.setCurrentValue(current);
            contributor.setPreviousValue(previous);
            contributor.setAbsoluteChange(absoluteChange);
            contributor.setPercentageChange(percentageChange);

            contributors.add(contributor);
        }

        contributors.sort(
                Comparator.comparing(
                        ReportChangeContributorData::getAbsoluteChange,
                        Comparator.nullsFirst(
                                Comparator.naturalOrder()
                        )
                ).reversed()
        );

        if (contributors.size() > MAX_CONTRIBUTORS) {
            return new ArrayList<>(
                    contributors.subList(
                            0,
                            MAX_CONTRIBUTORS
                    )
            );
        }

        return contributors;
    }

    private BigDecimal calculatePercentageChange(
            BigDecimal absoluteChange,
            BigDecimal previousValue
    ) {
        if (previousValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        if (previousValue.compareTo(BigDecimal.ZERO) < 0) {
            return null;
        }

        return absoluteChange
                .divide(
                        previousValue,
                        6,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal normalize(BigDecimal value) {
        return value == null
                ? BigDecimal.ZERO
                : value.setScale(2, RoundingMode.HALF_UP);
    }
}
