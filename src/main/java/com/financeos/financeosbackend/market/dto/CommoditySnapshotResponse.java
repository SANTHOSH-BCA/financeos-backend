package com.financeos.financeosbackend.market.dto;

import com.financeos.financeosbackend.market.enums.MarketStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CommoditySnapshotResponse(

        String symbol,

        String name,

        String currency,

        BigDecimal currentValue,

        BigDecimal dailyChange,

        BigDecimal dailyChangePercentage,

        BigDecimal previousClose,

        MarketStatus status,

        LocalDateTime observedAt,

        String source,

        boolean delayed

) {
}