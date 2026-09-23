package com.financeos.financeosbackend.market.enums;

public enum CryptoAsset {

    BITCOIN(
            "BTC",
            "Bitcoin",
            "USD"
    ),

    ETHEREUM(
            "ETH",
            "Ethereum",
            "USD"
    );

    private final String symbol;
    private final String displayName;
    private final String currency;

    CryptoAsset(
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