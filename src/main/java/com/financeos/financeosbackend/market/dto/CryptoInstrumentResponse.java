package com.financeos.financeosbackend.market.dto;

public record CryptoInstrumentResponse(

        String symbol,

        String name,

        String currency,

        String region

) {
}