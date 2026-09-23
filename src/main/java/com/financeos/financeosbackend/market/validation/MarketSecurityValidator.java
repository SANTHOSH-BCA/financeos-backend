package com.financeos.financeosbackend.market.validation;

import org.springframework.stereotype.Component;

@Component
public class MarketSecurityValidator {

    public void validateSymbol(String symbol) {

        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException(
                    "Market symbol must not be blank"
            );
        }

        if (symbol.length() > 50) {
            throw new IllegalArgumentException(
                    "Market symbol is too long"
            );
        }
    }
}