package com.financeos.financeosbackend.market.dto;

import com.financeos.financeosbackend.market.enums.DataFreshness;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.enums.MarketStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketSnapshotResponse(
        String symbol,
        String name,
        MarketCategory category,
        MarketRegion region,
        String currency,
        BigDecimal currentValue,
        BigDecimal dailyChange,
        BigDecimal dailyChangePercentage,
        BigDecimal previousClose,
        MarketStatus status,
        LocalDateTime observedAt,
        String source,
        Boolean delayed,
        DataFreshness freshness
) {
}