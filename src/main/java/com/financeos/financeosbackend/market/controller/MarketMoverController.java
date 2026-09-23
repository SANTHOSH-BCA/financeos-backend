package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.MarketMoverResponse;
import com.financeos.financeosbackend.market.service.MarketMoverService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/market/movers")
public class MarketMoverController {

    private final MarketMoverService marketMoverService;

    public MarketMoverController(MarketMoverService marketMoverService) {
        this.marketMoverService = marketMoverService;
    }

    @GetMapping("/gainers")
    public List<MarketMoverResponse> getTopGainers() {
        return marketMoverService.getTopGainers();
    }

    @GetMapping("/decliners")
    public List<MarketMoverResponse> getTopDecliners() {
        return marketMoverService.getTopDecliners();
    }
}