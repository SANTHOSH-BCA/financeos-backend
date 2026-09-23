package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.GlobalMarketInstrumentResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.GlobalMarketIndex;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GlobalMarketService {

    private final MarketInstrumentService marketInstrumentService;

    public GlobalMarketService(
            MarketInstrumentService marketInstrumentService) {

        this.marketInstrumentService = marketInstrumentService;
    }

    public List<GlobalMarketInstrumentResponse> getGlobalIndices() {

        return Arrays.stream(GlobalMarketIndex.values())
                .map(this::toResponse)
                .toList();
    }

    private GlobalMarketInstrumentResponse toResponse(
            GlobalMarketIndex index) {

        MarketInstrument instrument =
                marketInstrumentService.getBySymbol(index.getSymbol());

        return new GlobalMarketInstrumentResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCurrency(),
                instrument.getRegion().name()
        );
    }
}