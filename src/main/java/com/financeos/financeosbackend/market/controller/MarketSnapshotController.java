package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.MarketSnapshotResponse;
import com.financeos.financeosbackend.market.service.MarketSnapshotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market/snapshots")
public class MarketSnapshotController {

    private final MarketSnapshotService marketSnapshotService;

    public MarketSnapshotController(
            MarketSnapshotService marketSnapshotService
    ) {
        this.marketSnapshotService = marketSnapshotService;
    }

    @GetMapping
    public List<MarketSnapshotResponse> getSnapshots() {
        return marketSnapshotService.getSnapshots();
    }

    @GetMapping("/{symbol}")
    public MarketSnapshotResponse getSnapshot(
            @PathVariable String symbol
    ) {
        return marketSnapshotService.getSnapshot(symbol);
    }
}