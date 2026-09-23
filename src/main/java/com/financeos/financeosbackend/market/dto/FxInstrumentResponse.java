package com.financeos.financeosbackend.market.dto;

public record FxInstrumentResponse(
        String symbol,
        String baseCurrency,
        String quoteCurrency
) {
}