package com.financeos.financeosbackend.market.provider;

import com.financeos.financeosbackend.market.dto.MarketData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultMarketDataProvider implements MarketDataProvider {

    @Override
    public List<MarketData> fetchMarketData(List<String> symbols) {

        throw new MarketDataProviderException(
                "No external market data provider has been configured"
        );
    }

    @Override
    public String getProviderName() {
        return "UNCONFIGURED";
    }
}