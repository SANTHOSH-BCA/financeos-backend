package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.CommodityInstrumentResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.CommodityType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CommodityMarketService {

    private final MarketInstrumentService marketInstrumentService;

    public CommodityMarketService(
            MarketInstrumentService marketInstrumentService) {

        this.marketInstrumentService = marketInstrumentService;
    }

    public List<CommodityInstrumentResponse> getCommodities() {

        return Arrays.stream(CommodityType.values())
                .map(this::toResponse)
                .toList();
    }

    private CommodityInstrumentResponse toResponse(
            CommodityType commodity) {

        MarketInstrument instrument =
                marketInstrumentService.getBySymbol(
                        commodity.getSymbol()
                );

        return new CommodityInstrumentResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCurrency(),
                instrument.getRegion().name()
        );
    }
}