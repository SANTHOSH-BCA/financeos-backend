package com.financeos.financeosbackend.market.enums;

public enum GlobalMarketIndex {

    S_AND_P_500(
            "SP500",
            "S&P 500",
            "USD",
            MarketRegion.UNITED_STATES
    ),

    NASDAQ(
            "NASDAQ",
            "NASDAQ",
            "USD",
            MarketRegion.UNITED_STATES
    ),

    TAIWAN_WEIGHTED(
            "TWII",
            "Taiwan Weighted Index",
            "TWD",
            MarketRegion.TAIWAN
    );

    private final String symbol;
    private final String displayName;
    private final String currency;
    private final MarketRegion region;

    GlobalMarketIndex(
            String symbol,
            String displayName,
            String currency,
            MarketRegion region) {

        this.symbol = symbol;
        this.displayName = displayName;
        this.currency = currency;
        this.region = region;
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

    public MarketRegion getRegion() {
        return region;
    }
}