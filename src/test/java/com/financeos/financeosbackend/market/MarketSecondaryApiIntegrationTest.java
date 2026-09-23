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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MarketSecondaryApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MarketInstrumentRepository instrumentRepository;

    @Autowired
    private MarketObservationRepository observationRepository;

    @Test
    @WithMockUser
    void shouldReturnMarketHistoryThroughApi() throws Exception {

        String symbol = createTestMarketData(
                "TEST_HISTORY_"
        );

        mockMvc.perform(
                        get("/api/market/history/{symbol}", symbol)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnMarketMoversThroughApi() throws Exception {

        createTestMarketData(
                "TEST_MOVER_"
        );

        mockMvc.perform(
                        get("/api/market/movers/gainers")
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/api/market/movers/decliners")
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnInvestmentMarketContextThroughApi()
            throws Exception {

        String symbol = createTestMarketData(
                "TEST_CONTEXT_"
        );

        mockMvc.perform(
                        get(
                                "/api/market/investment-context/{symbol}",
                                symbol
                        )
                )
                .andExpect(status().isOk());
    }

    private String createTestMarketData(String prefix) {

        String symbol =
                prefix
                        + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8);

        MarketInstrument instrument =
                new MarketInstrument();

        instrument.setSymbol(symbol);
        instrument.setName("Test Market Data");
        instrument.setCategory(
                MarketCategory.INDIAN_INDEX
        );
        instrument.setRegion(
                MarketRegion.INDIA
        );
        instrument.setCurrency("INR");
        instrument.setExternalIdentifier(symbol);
        instrument.setActive(true);

        MarketInstrument savedInstrument =
                instrumentRepository.save(instrument);

        MarketObservation observation =
                new MarketObservation();

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

        return symbol;
    }
}