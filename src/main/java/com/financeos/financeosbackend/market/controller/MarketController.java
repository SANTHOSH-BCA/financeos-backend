package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.MarketSnapshotResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.service.MarketInstrumentService;
import com.financeos.financeosbackend.market.service.MarketSnapshotService;
import com.financeos.financeosbackend.market.validation.MarketSecurityValidator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketSnapshotService marketSnapshotService;
    private final MarketInstrumentService marketInstrumentService;
    private final MarketSecurityValidator marketSecurityValidator;

    public MarketController(
            MarketSnapshotService marketSnapshotService,
            MarketInstrumentService marketInstrumentService,
            MarketSecurityValidator marketSecurityValidator
    ) {
        this.marketSnapshotService = marketSnapshotService;
        this.marketInstrumentService = marketInstrumentService;
        this.marketSecurityValidator = marketSecurityValidator;
    }

    @GetMapping("/snapshot")
    public List<MarketSnapshotResponse> getMarketSnapshot() {
        return marketSnapshotService.getSnapshots();
    }

    @GetMapping("/snapshot/{symbol}")
    public MarketSnapshotResponse getMarketSnapshot(
            @PathVariable String symbol
    ) {
        marketSecurityValidator.validateSymbol(symbol);
        return marketSnapshotService.getSnapshot(symbol);
    }

    @GetMapping("/instruments")
    public List<MarketInstrument> getActiveInstruments() {
        return marketInstrumentService.getActiveInstruments();
    }

    @GetMapping("/instruments/{symbol}")
    public MarketInstrument getInstrument(
            @PathVariable String symbol
    ) {
        marketSecurityValidator.validateSymbol(symbol);
        return marketInstrumentService.getBySymbol(symbol);
    }

    @GetMapping("/overview")
    public List<MarketSnapshotResponse> getMarketOverview() {
        return marketSnapshotService.getSnapshots();
    }
}