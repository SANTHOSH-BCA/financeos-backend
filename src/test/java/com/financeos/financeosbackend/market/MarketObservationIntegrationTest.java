package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
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
class MarketObservationIntegrationTest {

    @Autowired
    private MarketInstrumentRepository instrumentRepository;

    @Autowired
    private MarketObservationRepository observationRepository;

    @BeforeEach
    void cleanupTestData() {

        instrumentRepository.findBySymbol("TEST_OBS")
                .ifPresent(instrument -> {

                    observationRepository
                            .findByInstrumentOrderByObservedAtDesc(instrument)
                            .forEach(observationRepository::delete);

                    instrumentRepository.delete(instrument);
                });
    }

    @Test
    void shouldPersistAndRetrieveMarketObservation() {

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol("TEST_OBS");
        instrument.setName("Test Observation");
        instrument.setCategory(MarketCategory.INDIAN_INDEX);
        instrument.setRegion(MarketRegion.INDIA);
        instrument.setCurrency("INR");
        instrument.setExternalIdentifier("TEST_OBS");
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
                LocalDateTime.now()
        );
        observation.setSource("TEST");
        observation.setDelayed(false);

        MarketObservation savedObservation =
                observationRepository.save(observation);

        assertNotNull(savedObservation.getId());

        var observations =
                observationRepository
                        .findByInstrumentOrderByObservedAtDesc(
                                savedInstrument
                        );

        assertFalse(observations.isEmpty());

        assertEquals(
                new BigDecimal("25000"),
                observations.get(0).getCurrentValue()
        );

        assertEquals(
                "TEST",
                observations.get(0).getSource()
        );
    }
}