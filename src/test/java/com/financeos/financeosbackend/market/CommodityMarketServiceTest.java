package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.dto.CommodityInstrumentResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.service.CommodityMarketService;
import com.financeos.financeosbackend.market.service.MarketInstrumentService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommodityMarketServiceTest {

    @Test
    void shouldReturnSupportedCommodities() {

        MarketInstrumentService instrumentService =
                mock(MarketInstrumentService.class);

        MarketInstrument gold = createInstrument(
                "GOLD",
                "Gold"
        );

        MarketInstrument silver = createInstrument(
                "SILVER",
                "Silver"
        );

        MarketInstrument crudeOil = createInstrument(
                "CRUDE_OIL",
                "Crude Oil"
        );

        when(instrumentService.getBySymbol("GOLD"))
                .thenReturn(gold);

        when(instrumentService.getBySymbol("SILVER"))
                .thenReturn(silver);

        when(instrumentService.getBySymbol("CRUDE_OIL"))
                .thenReturn(crudeOil);

        CommodityMarketService service =
                new CommodityMarketService(instrumentService);

        List<CommodityInstrumentResponse> result =
                service.getCommodities();

        assertEquals(3, result.size());
        assertEquals("GOLD", result.get(0).symbol());
        assertEquals("SILVER", result.get(1).symbol());
        assertEquals("CRUDE_OIL", result.get(2).symbol());
    }

    private MarketInstrument createInstrument(
            String symbol,
            String name) {

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol(symbol);
        instrument.setName(name);
        instrument.setCategory(MarketCategory.COMMODITY);
        instrument.setRegion(MarketRegion.GLOBAL);
        instrument.setCurrency("USD");

        return instrument;
    }
}