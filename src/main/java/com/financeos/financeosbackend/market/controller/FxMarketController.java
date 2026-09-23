package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.FxInstrumentResponse;
import com.financeos.financeosbackend.market.dto.FxSnapshotResponse;
import com.financeos.financeosbackend.market.service.FxMarketService;
import com.financeos.financeosbackend.market.service.FxSnapshotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market/fx")
public class FxMarketController {

    private final FxMarketService fxMarketService;
    private final FxSnapshotService fxSnapshotService;

    public FxMarketController(
            FxMarketService fxMarketService,
            FxSnapshotService fxSnapshotService
    ) {
        this.fxMarketService = fxMarketService;
        this.fxSnapshotService = fxSnapshotService;
    }

    @GetMapping
    public List<FxInstrumentResponse> getSupportedPairs() {
        return fxMarketService.getSupportedPairs();
    }

    @GetMapping("/snapshots")
    public List<FxSnapshotResponse> getSnapshots() {
        return fxSnapshotService.getSnapshots();
    }

    @GetMapping("/snapshots/{symbol}")
    public FxSnapshotResponse getSnapshot(
            @PathVariable String symbol
    ) {
        return fxSnapshotService.getSnapshot(symbol);
    }
}