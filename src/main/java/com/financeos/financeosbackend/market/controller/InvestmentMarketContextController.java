package com.financeos.financeosbackend.market.controller;

import com.financeos.financeosbackend.market.dto.InvestmentMarketContextResponse;
import com.financeos.financeosbackend.market.service.InvestmentMarketContextService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market/investment-context")
public class InvestmentMarketContextController {

    private final InvestmentMarketContextService service;

    public InvestmentMarketContextController(
            InvestmentMarketContextService service
    ) {
        this.service = service;
    }

    @GetMapping("/{symbol}")
    public InvestmentMarketContextResponse getContext(
            @PathVariable String symbol
    ) {
        return service.getContext(symbol);
    }
}