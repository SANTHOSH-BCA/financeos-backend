package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.service.MarketInstrumentService;
import com.financeos.financeosbackend.market.service.MarketSnapshotService;
import com.financeos.financeosbackend.market.validation.MarketSecurityValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MarketControllerTest {

    @Test
    void shouldCreateMarketController() {

        MarketSnapshotService snapshotService =
                Mockito.mock(MarketSnapshotService.class);

        MarketInstrumentService instrumentService =
                Mockito.mock(MarketInstrumentService.class);

        MarketSecurityValidator securityValidator =
                Mockito.mock(MarketSecurityValidator.class);

        MarketController controller =
                new MarketController(
                        snapshotService,
                        instrumentService,
                        securityValidator
                );

        assertNotNull(controller);
    }
}