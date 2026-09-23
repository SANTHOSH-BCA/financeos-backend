package com.financeos.financeosbackend.market.dto;

import com.financeos.financeosbackend.market.enums.DataFreshness;
import com.financeos.financeosbackend.market.enums.MarketCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvestmentMarketContextResponse(
        String symbol,
        String name,
        MarketCategory category,
        BigDecimal currentValue,
        BigDecimal dailyChangePercentage,
        LocalDateTime observedAt,
        DataFreshness freshness,
        String source
) {
}