package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.MarketHistoryResponse;
import com.financeos.financeosbackend.market.service.MarketHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market/history")
public class MarketHistoryController {

    private final MarketHistoryService marketHistoryService;

    public MarketHistoryController(
            MarketHistoryService marketHistoryService
    ) {
        this.marketHistoryService = marketHistoryService;
    }

    @GetMapping("/{symbol}")
    public List<MarketHistoryResponse> getHistory(
            @PathVariable String symbol
    ) {
        return marketHistoryService.getHistory(symbol);
    }
}