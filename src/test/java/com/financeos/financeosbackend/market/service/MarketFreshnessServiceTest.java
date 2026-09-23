package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.enums.DataFreshness;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarketFreshnessServiceTest {

    private final MarketFreshnessService service =
            new MarketFreshnessService();

    @Test
    void shouldReturnUnavailableWhenObservationTimeIsNull() {

        DataFreshness result = service.evaluate(null);

        assertEquals(DataFreshness.UNAVAILABLE, result);
    }

    @Test
    void shouldReturnFreshForRecentObservation() {

        LocalDateTime observedAt =
                LocalDateTime.now().minusMinutes(5);

        DataFreshness result = service.evaluate(observedAt);

        assertEquals(DataFreshness.FRESH, result);
    }

    @Test
    void shouldReturnStaleForOldObservation() {

        LocalDateTime observedAt =
                LocalDateTime.now().minusMinutes(30);

        DataFreshness result = service.evaluate(observedAt);

        assertEquals(DataFreshness.STALE, result);
    }

    @Test
    void shouldReturnFreshForFutureObservation() {

        LocalDateTime observedAt =
                LocalDateTime.now().plusMinutes(5);

        DataFreshness result = service.evaluate(observedAt);

        assertEquals(DataFreshness.FRESH, result);
    }
}