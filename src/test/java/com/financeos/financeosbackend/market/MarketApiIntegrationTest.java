package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MarketApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MarketInstrumentRepository instrumentRepository;

    @Autowired
    private MarketObservationRepository observationRepository;

    @Test
    @WithMockUser
    void shouldReturnMarketSnapshotThroughApi() throws Exception {

        String symbol = "TEST_API_" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8);

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol(symbol);
        instrument.setName("Test API Market");
        instrument.setCategory(MarketCategory.INDIAN_INDEX);
        instrument.setRegion(MarketRegion.INDIA);
        instrument.setCurrency("INR");
        instrument.setExternalIdentifier(symbol);
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
                LocalDateTime.now().minusMinutes(2)
        );
        observation.setSource("TEST_API");
        observation.setDelayed(false);

        observationRepository.save(observation);

        mockMvc.perform(
                        get("/api/market/snapshot/{symbol}", symbol)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value(symbol))
                .andExpect(jsonPath("$.name").value("Test API Market"))
                .andExpect(jsonPath("$.currentValue").value(25000))
                .andExpect(jsonPath("$.dailyChange").value(150))
                .andExpect(jsonPath("$.dailyChangePercentage").value(0.60))
                .andExpect(jsonPath("$.source").value("TEST_API"));
    }

    @Test
    @WithMockUser
    void shouldReturnMarketInstrumentsThroughApi() throws Exception {

        String symbol = "TEST_INST_" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8);

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol(symbol);
        instrument.setName("Test Instrument API");
        instrument.setCategory(MarketCategory.GLOBAL_INDEX);
        instrument.setRegion(MarketRegion.UNITED_STATES);
        instrument.setCurrency("USD");
        instrument.setExternalIdentifier(symbol);
        instrument.setActive(true);

        instrumentRepository.save(instrument);

        mockMvc.perform(
                        get("/api/market/instruments")
                )
                .andExpect(status().isOk());
    }
}