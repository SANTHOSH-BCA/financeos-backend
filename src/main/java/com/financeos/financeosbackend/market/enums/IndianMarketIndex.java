package com.financeos.financeosbackend.market.enums;

public enum IndianMarketIndex {

    NIFTY_50(
            "NIFTY50",
            "NIFTY 50",
            "INR"
    ),

    SENSEX(
            "SENSEX",
            "BSE SENSEX",
            "INR"
    );

    private final String symbol;
    private final String displayName;
    private final String currency;

    IndianMarketIndex(
            String symbol,
            String displayName,
            String currency) {

        this.symbol = symbol;
        this.displayName = displayName;
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCurrency() {
        return currency;
    }
}