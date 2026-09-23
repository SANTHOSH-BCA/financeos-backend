package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.GlobalMarketInstrumentResponse;
import com.financeos.financeosbackend.market.dto.GlobalMarketSnapshotResponse;
import com.financeos.financeosbackend.market.service.GlobalMarketService;
import com.financeos.financeosbackend.market.service.GlobalMarketSnapshotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market/global")
public class GlobalMarketController {

    private final GlobalMarketService globalMarketService;
    private final GlobalMarketSnapshotService snapshotService;

    public GlobalMarketController(
            GlobalMarketService globalMarketService,
            GlobalMarketSnapshotService snapshotService) {

        this.globalMarketService = globalMarketService;
        this.snapshotService = snapshotService;
    }

    @GetMapping("/indices")
    public List<GlobalMarketInstrumentResponse> getIndices() {
        return globalMarketService.getGlobalIndices();
    }

    @GetMapping("/snapshots")
    public List<GlobalMarketSnapshotResponse> getSnapshots() {
        return snapshotService.getSnapshots();
    }

    @GetMapping("/snapshots/{symbol}")
    public GlobalMarketSnapshotResponse getSnapshot(
            @PathVariable String symbol) {

        return snapshotService.getSnapshot(symbol);
    }
}