package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.DataFreshness;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import com.financeos.financeosbackend.market.service.MarketFreshnessService;
import com.financeos.financeosbackend.market.service.MarketSnapshotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MarketSnapshotIntegrationTest {

    @Autowired
    private MarketInstrumentRepository instrumentRepository;

    @Autowired
    private MarketObservationRepository observationRepository;

    @BeforeEach
    void cleanupTestData() {

        instrumentRepository.findBySymbol("TEST_SNAPSHOT")
                .ifPresent(instrument -> {

                    observationRepository
                            .findByInstrumentOrderByObservedAtDesc(instrument)
                            .forEach(observationRepository::delete);

                    instrumentRepository.delete(instrument);
                });
    }

    @Test
    void shouldBuildSnapshotFromLatestObservation() {

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol("TEST_SNAPSHOT");
        instrument.setName("Test Snapshot");
        instrument.setCategory(MarketCategory.INDIAN_INDEX);
        instrument.setRegion(MarketRegion.INDIA);
        instrument.setCurrency("INR");
        instrument.setExternalIdentifier("TEST_SNAPSHOT");
        instrument.setActive(true);

        MarketInstrument savedInstrument =
                instrumentRepository.save(instrument);

        MarketObservation observation = new MarketObservation();

        observation.setInstrument(savedInstrument);
        observation.setCurrentValue(
                new BigDecimal("25000")
        );
        observation.setDailyChange(
                new BigDecimal("150")
        );
        observation.setDailyChangePercentage(
                new BigDecimal("0.60")
        );
        observation.setPreviousClose(
                new BigDecimal("24850")
        );
        observation.setObservedAt(
                LocalDateTime.now().minusMinutes(5)
        );
        observation.setSource("TEST");
        observation.setDelayed(false);

        observationRepository.save(observation);

        MarketSnapshotService service =
                new MarketSnapshotService(
                        instrumentRepository,
                        observationRepository,
                        new MarketFreshnessService()
                );

        var snapshot =
                service.getSnapshot("TEST_SNAPSHOT");

        assertEquals(
                "TEST_SNAPSHOT",
                snapshot.symbol()
        );

        assertEquals(
                new BigDecimal("25000"),
                snapshot.currentValue()
        );

        assertEquals(
                new BigDecimal("0.60"),
                snapshot.dailyChangePercentage()
        );

        assertEquals(
                DataFreshness.FRESH,
                snapshot.freshness()
        );

        assertEquals(
                "TEST",
                snapshot.source()
        );
    }
}