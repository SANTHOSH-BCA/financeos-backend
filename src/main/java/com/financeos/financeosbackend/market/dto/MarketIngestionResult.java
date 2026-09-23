package com.financeos.financeosbackend.market.dto;

import java.util.List;

public record MarketIngestionResult(
        int requestedCount,
        int processedCount,
        List<String> processedSymbols
) {
}