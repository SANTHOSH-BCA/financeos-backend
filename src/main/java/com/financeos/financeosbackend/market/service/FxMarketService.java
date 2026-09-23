package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.FxInstrumentResponse;
import com.financeos.financeosbackend.market.entity.FxPair;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FxMarketService {

    public List<FxInstrumentResponse> getSupportedPairs() {
        return Arrays.stream(FxPair.values())
                .map(pair -> new FxInstrumentResponse(
                        pair.getSymbol(),
                        pair.getBaseCurrency(),
                        pair.getQuoteCurrency()
                ))
                .toList();
    }
}