package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.CryptoInstrumentResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.CryptoAsset;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CryptoMarketService {

    private final MarketInstrumentService marketInstrumentService;

    public CryptoMarketService(
            MarketInstrumentService marketInstrumentService) {

        this.marketInstrumentService = marketInstrumentService;
    }

    public List<CryptoInstrumentResponse> getCryptoAssets() {

        return Arrays.stream(CryptoAsset.values())
                .map(this::toResponse)
                .toList();
    }

    private CryptoInstrumentResponse toResponse(
            CryptoAsset asset) {

        MarketInstrument instrument =
                marketInstrumentService.getBySymbol(
                        asset.getSymbol()
                );

        return new CryptoInstrumentResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCurrency(),
                instrument.getRegion().name()
        );
    }
}