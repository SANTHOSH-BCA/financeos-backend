package com.financeos.financeosbackend.market.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketData(

        String symbol,

        String name,

        BigDecimal currentValue,

        BigDecimal dailyChange,

        BigDecimal dailyChangePercentage,

        BigDecimal previousClose,

        LocalDateTime observedAt,

        String source,

        boolean delayed

) {
}