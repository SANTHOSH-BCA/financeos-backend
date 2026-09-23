package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.FxPair;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FxMarketInitializer implements CommandLineRunner {

    private final MarketInstrumentRepository marketInstrumentRepository;

    public FxMarketInitializer(
            MarketInstrumentRepository marketInstrumentRepository
    ) {
        this.marketInstrumentRepository = marketInstrumentRepository;
    }

    @Override
    public void run(String... args) {

        for (FxPair pair : FxPair.values()) {

            if (marketInstrumentRepository
                    .findBySymbol(pair.getSymbol())
                    .isPresent()) {
                continue;
            }

            MarketInstrument instrument = new MarketInstrument();

            instrument.setSymbol(pair.getSymbol());
            instrument.setName(pair.getSymbol());
            instrument.setCategory(MarketCategory.FX);
            instrument.setRegion(MarketRegion.GLOBAL);
            instrument.setCurrency(pair.getQuoteCurrency());
            instrument.setExternalIdentifier(pair.getSymbol());
            instrument.setActive(true);

            marketInstrumentRepository.save(instrument);
        }
    }
}