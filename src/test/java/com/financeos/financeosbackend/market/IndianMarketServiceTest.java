package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.dto.IndianMarketInstrumentResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.service.IndianMarketService;
import com.financeos.financeosbackend.market.service.MarketInstrumentService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndianMarketServiceTest {

    @Test
    void shouldReturnIndianMarketIndices() {

        MarketInstrumentService instrumentService =
                mock(MarketInstrumentService.class);

        MarketInstrument nifty = new MarketInstrument();
        nifty.setSymbol("NIFTY50");
        nifty.setName("NIFTY 50");
        nifty.setCategory(MarketCategory.INDIAN_INDEX);
        nifty.setRegion(MarketRegion.INDIA);
        nifty.setCurrency("INR");

        MarketInstrument sensex = new MarketInstrument();
        sensex.setSymbol("SENSEX");
        sensex.setName("BSE SENSEX");
        sensex.setCategory(MarketCategory.INDIAN_INDEX);
        sensex.setRegion(MarketRegion.INDIA);
        sensex.setCurrency("INR");

        when(instrumentService.getBySymbol("NIFTY50"))
                .thenReturn(nifty);

        when(instrumentService.getBySymbol("SENSEX"))
                .thenReturn(sensex);

        IndianMarketService service =
                new IndianMarketService(instrumentService);

        List<IndianMarketInstrumentResponse> result =
                service.getIndianMarketIndices();

        assertEquals(2, result.size());
        assertEquals("NIFTY50", result.get(0).symbol());
        assertEquals("SENSEX", result.get(1).symbol());
    }
}