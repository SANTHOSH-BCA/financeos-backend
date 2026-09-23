package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.CryptoInstrumentResponse;
import com.financeos.financeosbackend.market.dto.CryptoSnapshotResponse;
import com.financeos.financeosbackend.market.service.CryptoMarketService;
import com.financeos.financeosbackend.market.service.CryptoSnapshotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market/crypto")
public class CryptoMarketController {

    private final CryptoMarketService cryptoMarketService;
    private final CryptoSnapshotService snapshotService;

    public CryptoMarketController(
            CryptoMarketService cryptoMarketService,
            CryptoSnapshotService snapshotService) {

        this.cryptoMarketService = cryptoMarketService;
        this.snapshotService = snapshotService;
    }

    @GetMapping
    public List<CryptoInstrumentResponse> getCryptoAssets() {
        return cryptoMarketService.getCryptoAssets();
    }

    @GetMapping("/snapshots")
    public List<CryptoSnapshotResponse> getSnapshots() {
        return snapshotService.getSnapshots();
    }

    @GetMapping("/snapshots/{symbol}")
    public CryptoSnapshotResponse getSnapshot(
            @PathVariable String symbol) {

        return snapshotService.getSnapshot(symbol);
    }
}