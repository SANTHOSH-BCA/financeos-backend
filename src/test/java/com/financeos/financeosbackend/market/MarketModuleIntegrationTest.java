package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketModuleIntegrationTest {

    @Autowired
    private MarketInstrumentRepository marketInstrumentRepository;

    @Test
    void shouldLoadMarketModuleContext() {

        assertNotNull(marketInstrumentRepository);
    }

    @Test
    void shouldPersistAndRetrieveMarketInstrument() {

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol("TEST_FX");
        instrument.setName("Test FX");
        instrument.setCategory(MarketCategory.FX);
        instrument.setRegion(MarketRegion.GLOBAL);
        instrument.setCurrency("INR");
        instrument.setExternalIdentifier("TEST_FX");
        instrument.setActive(true);

        MarketInstrument saved =
                marketInstrumentRepository.save(instrument);

        assertNotNull(saved.getId());

        MarketInstrument retrieved =
                marketInstrumentRepository
                        .findBySymbol("TEST_FX")
                        .orElseThrow();

        assertEquals("TEST_FX", retrieved.getSymbol());
        assertEquals("Test FX", retrieved.getName());
        assertEquals(MarketCategory.FX, retrieved.getCategory());
        assertEquals(MarketRegion.GLOBAL, retrieved.getRegion());
        assertEquals("INR", retrieved.getCurrency());

        marketInstrumentRepository.delete(retrieved);
    }
}