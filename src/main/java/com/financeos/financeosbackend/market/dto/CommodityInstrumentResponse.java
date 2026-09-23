package com.financeos.financeosbackend.market.dto;

public record CommodityInstrumentResponse(

        String symbol,

        String name,

        String currency,

        String region

) {
}