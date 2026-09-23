package com.financeos.financeosbackend.market.entity;

public enum FxPair {

    USD_INR("USD/INR", "USD", "INR"),
    EUR_INR("EUR/INR", "EUR", "INR"),
    GBP_INR("GBP/INR", "GBP", "INR");

    private final String symbol;
    private final String baseCurrency;
    private final String quoteCurrency;

    FxPair(String symbol, String baseCurrency, String quoteCurrency) {
        this.symbol = symbol;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }
}