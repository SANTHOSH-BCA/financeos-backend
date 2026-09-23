package com.financeos.financeosbackend.market.dto;

public record GlobalMarketInstrumentResponse(

        String symbol,

        String name,

        String currency,

        String region

) {
}