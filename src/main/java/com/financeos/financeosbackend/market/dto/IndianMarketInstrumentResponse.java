package com.financeos.financeosbackend.market.dto;

import com.financeos.financeosbackend.market.enums.MarketStatus;

public record IndianMarketInstrumentResponse(

        String symbol,

        String name,

        String currency,

        MarketStatus status

) {
}