package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.CommodityInstrumentResponse;
import com.financeos.financeosbackend.market.dto.CommoditySnapshotResponse;
import com.financeos.financeosbackend.market.service.CommodityMarketService;
import com.financeos.financeosbackend.market.service.CommoditySnapshotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market/commodities")
public class CommodityMarketController {

    private final CommodityMarketService commodityMarketService;
    private final CommoditySnapshotService snapshotService;

    public CommodityMarketController(
            CommodityMarketService commodityMarketService,
            CommoditySnapshotService snapshotService) {

        this.commodityMarketService = commodityMarketService;
        this.snapshotService = snapshotService;
    }

    @GetMapping
    public List<CommodityInstrumentResponse> getCommodities() {
        return commodityMarketService.getCommodities();
    }

    @GetMapping("/snapshots")
    public List<CommoditySnapshotResponse> getSnapshots() {
        return snapshotService.getSnapshots();
    }

    @GetMapping("/snapshots/{symbol}")
    public CommoditySnapshotResponse getSnapshot(
            @PathVariable String symbol) {

        return snapshotService.getSnapshot(symbol);
    }
}