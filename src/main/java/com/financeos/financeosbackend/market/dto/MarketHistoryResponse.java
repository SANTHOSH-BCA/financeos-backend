package com.financeos.financeosbackend.market.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketHistoryResponse(
        String symbol,
        BigDecimal currentValue,
        BigDecimal dailyChange,
        BigDecimal dailyChangePercentage,
        BigDecimal previousClose,
        LocalDateTime observedAt,
        String source,
        Boolean delayed
) {
}