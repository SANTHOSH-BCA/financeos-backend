package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.enums.DataFreshness;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class MarketFreshnessService {

    private static final long FRESHNESS_THRESHOLD_MINUTES = 15;

    public DataFreshness evaluate(LocalDateTime observedAt) {

        if (observedAt == null) {
            return DataFreshness.UNAVAILABLE;
        }

        long ageMinutes = Duration.between(
                observedAt,
                LocalDateTime.now()
        ).toMinutes();

        if (ageMinutes < 0) {
            return DataFreshness.FRESH;
        }

        if (ageMinutes <= FRESHNESS_THRESHOLD_MINUTES) {
            return DataFreshness.FRESH;
        }

        return DataFreshness.STALE;
    }
}