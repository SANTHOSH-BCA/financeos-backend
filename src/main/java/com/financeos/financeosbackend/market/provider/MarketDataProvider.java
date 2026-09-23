package com.financeos.financeosbackend.market.provider;

import com.financeos.financeosbackend.market.dto.MarketData;

import java.util.List;

public interface MarketDataProvider {

    List<MarketData> fetchMarketData(List<String> symbols);

    String getProviderName();
}