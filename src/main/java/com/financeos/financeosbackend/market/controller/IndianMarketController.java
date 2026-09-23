package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.IndianMarketInstrumentResponse;
import com.financeos.financeosbackend.market.dto.IndianMarketSnapshotResponse;
import com.financeos.financeosbackend.market.service.IndianMarketService;
import com.financeos.financeosbackend.market.service.IndianMarketSnapshotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market/india")
public class IndianMarketController {

    private final IndianMarketService indianMarketService;
    private final IndianMarketSnapshotService snapshotService;

    public IndianMarketController(
            IndianMarketService indianMarketService,
            IndianMarketSnapshotService snapshotService) {

        this.indianMarketService = indianMarketService;
        this.snapshotService = snapshotService;
    }

    @GetMapping("/indices")
    public List<IndianMarketInstrumentResponse> getIndices() {
        return indianMarketService.getIndianMarketIndices();
    }

    @GetMapping("/snapshots")
    public List<IndianMarketSnapshotResponse> getSnapshots() {
        return snapshotService.getSnapshots();
    }

    @GetMapping("/snapshots/{symbol}")
    public IndianMarketSnapshotResponse getSnapshot(
            @PathVariable String symbol) {

        return snapshotService.getSnapshot(symbol);
    }
}