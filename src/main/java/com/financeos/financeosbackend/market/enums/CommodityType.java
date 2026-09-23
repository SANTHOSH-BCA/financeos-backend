package com.financeos.financeosbackend.market.enums;

public enum CommodityType {

    GOLD(
            "GOLD",
            "Gold",
            "USD"
    ),

    SILVER(
            "SILVER",
            "Silver",
            "USD"
    ),

    CRUDE_OIL(
            "CRUDE_OIL",
            "Crude Oil",
            "USD"
    );

    private final String symbol;
    private final String displayName;
    private final String currency;

    CommodityType(
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