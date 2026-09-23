package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.IndianMarketInstrumentResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.IndianMarketIndex;
import com.financeos.financeosbackend.market.enums.MarketStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class IndianMarketService {

    private final MarketInstrumentService marketInstrumentService;

    public IndianMarketService(
            MarketInstrumentService marketInstrumentService) {

        this.marketInstrumentService = marketInstrumentService;
    }

    public List<IndianMarketInstrumentResponse> getIndianMarketIndices() {

        return Arrays.stream(IndianMarketIndex.values())
                .map(this::toResponse)
                .toList();
    }

    private IndianMarketInstrumentResponse toResponse(
            IndianMarketIndex index) {

        MarketInstrument instrument =
                findInstrument(index.getSymbol());

        return new IndianMarketInstrumentResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCurrency(),
                MarketStatus.UNKNOWN
        );
    }

    private MarketInstrument findInstrument(String symbol) {

        return marketInstrumentService.getBySymbol(symbol);
    }
}