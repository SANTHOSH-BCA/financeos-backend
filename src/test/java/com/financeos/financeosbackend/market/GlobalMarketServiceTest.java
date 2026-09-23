package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.dto.GlobalMarketInstrumentResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.service.GlobalMarketService;
import com.financeos.financeosbackend.market.service.MarketInstrumentService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalMarketServiceTest {

    @Test
    void shouldReturnGlobalMarketIndices() {

        MarketInstrumentService instrumentService =
                mock(MarketInstrumentService.class);

        MarketInstrument sp500 = new MarketInstrument();
        sp500.setSymbol("SP500");
        sp500.setName("S&P 500");
        sp500.setCategory(MarketCategory.GLOBAL_INDEX);
        sp500.setRegion(MarketRegion.UNITED_STATES);
        sp500.setCurrency("USD");

        when(instrumentService.getBySymbol("SP500"))
                .thenReturn(sp500);

        MarketInstrument nasdaq = new MarketInstrument();
        nasdaq.setSymbol("NASDAQ");
        nasdaq.setName("NASDAQ");
        nasdaq.setCategory(MarketCategory.GLOBAL_INDEX);
        nasdaq.setRegion(MarketRegion.UNITED_STATES);
        nasdaq.setCurrency("USD");

        when(instrumentService.getBySymbol("NASDAQ"))
                .thenReturn(nasdaq);

        MarketInstrument taiwan = new MarketInstrument();
        taiwan.setSymbol("TWII");
        taiwan.setName("Taiwan Weighted Index");
        taiwan.setCategory(MarketCategory.GLOBAL_INDEX);
        taiwan.setRegion(MarketRegion.TAIWAN);
        taiwan.setCurrency("TWD");

        when(instrumentService.getBySymbol("TWII"))
                .thenReturn(taiwan);

        GlobalMarketService service =
                new GlobalMarketService(instrumentService);

        List<GlobalMarketInstrumentResponse> result =
                service.getGlobalIndices();

        assertEquals(3, result.size());
        assertEquals("SP500", result.get(0).symbol());
        assertEquals("NASDAQ", result.get(1).symbol());
        assertEquals("TWII", result.get(2).symbol());
    }
}