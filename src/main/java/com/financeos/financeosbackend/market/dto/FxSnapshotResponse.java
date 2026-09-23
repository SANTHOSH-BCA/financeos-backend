package com.financeos.financeosbackend.market.dto;

import com.financeos.financeosbackend.market.enums.MarketStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FxSnapshotResponse(
        String symbol,
        String baseCurrency,
        String quoteCurrency,
        BigDecimal currentValue,
        BigDecimal dailyChange,
        BigDecimal dailyChangePercentage,
        BigDecimal previousClose,
        MarketStatus status,
        LocalDateTime observedAt,
        String source,
        Boolean delayed
) {
}