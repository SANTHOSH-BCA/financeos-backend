package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.CryptoAsset;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CryptoMarketInitializer implements CommandLineRunner {

    private final MarketInstrumentRepository instrumentRepository;

    public CryptoMarketInitializer(
            MarketInstrumentRepository instrumentRepository) {

        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public void run(String... args) {

        for (CryptoAsset asset : CryptoAsset.values()) {
            createIfMissing(asset);
        }
    }

    private void createIfMissing(CryptoAsset asset) {

        if (instrumentRepository
                .findBySymbol(asset.getSymbol())
                .isPresent()) {
            return;
        }

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol(asset.getSymbol());
        instrument.setName(asset.getDisplayName());
        instrument.setCategory(MarketCategory.CRYPTO);
        instrument.setRegion(MarketRegion.GLOBAL);
        instrument.setCurrency(asset.getCurrency());
        instrument.setActive(true);

        instrumentRepository.save(instrument);
    }
}