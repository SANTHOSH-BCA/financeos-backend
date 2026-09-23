package com.financeos.financeosbackend.market.validation;

import com.financeos.financeosbackend.market.dto.MarketData;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;


@Component
public class MarketDataValidator {

    public void validate(MarketData marketData) {

        if (marketData == null) {
            throw new IllegalArgumentException("Market data cannot be null");
        }

        if (marketData.symbol() == null || marketData.symbol().isBlank()) {
            throw new IllegalArgumentException("Market symbol cannot be blank");
        }

        if (marketData.currentValue() == null) {
            throw new IllegalArgumentException("Current market value cannot be null");
        }

        if (marketData.currentValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Current market value cannot be negative"
            );
        }

        if (marketData.observedAt() == null) {
            throw new IllegalArgumentException(
                    "Market observation time cannot be null"
            );
        }

        if (marketData.source() == null || marketData.source().isBlank()) {
            throw new IllegalArgumentException(
                    "Market data source cannot be blank"
            );
        }
    }
}