package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.dto.CryptoInstrumentResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.service.CryptoMarketService;
import com.financeos.financeosbackend.market.service.MarketInstrumentService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CryptoMarketServiceTest {

    @Test
    void shouldReturnSupportedCryptoAssets() {

        MarketInstrumentService instrumentService =
                mock(MarketInstrumentService.class);

        MarketInstrument bitcoin =
                createInstrument("BTC", "Bitcoin");

        MarketInstrument ethereum =
                createInstrument("ETH", "Ethereum");

        when(instrumentService.getBySymbol("BTC"))
                .thenReturn(bitcoin);

        when(instrumentService.getBySymbol("ETH"))
                .thenReturn(ethereum);

        CryptoMarketService service =
                new CryptoMarketService(instrumentService);

        List<CryptoInstrumentResponse> result =
                service.getCryptoAssets();

        assertEquals(2, result.size());
        assertEquals("BTC", result.get(0).symbol());
        assertEquals("ETH", result.get(1).symbol());
    }

    private MarketInstrument createInstrument(
            String symbol,
            String name) {

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol(symbol);
        instrument.setName(name);
        instrument.setCategory(MarketCategory.CRYPTO);
        instrument.setRegion(MarketRegion.GLOBAL);
        instrument.setCurrency("USD");

        return instrument;
    }
}