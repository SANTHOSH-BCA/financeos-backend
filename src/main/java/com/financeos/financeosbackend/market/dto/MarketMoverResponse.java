package com.financeos.financeosbackend.market.dto;

import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketMoverResponse(
        String symbol,
        String name,
        MarketCategory category,
        MarketRegion region,
        BigDecimal currentValue,
        BigDecimal dailyChange,
        BigDecimal dailyChangePercentage,
        LocalDateTime observedAt
) {
}