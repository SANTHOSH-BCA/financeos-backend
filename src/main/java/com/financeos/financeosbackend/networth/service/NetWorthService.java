package com.financeos.financeosbackend.networth.service;

import com.financeos.financeosbackend.asset.entity.Asset;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import com.financeos.financeosbackend.asset.repository.AssetRepository;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class NetWorthService {

    private final AssetRepository assetRepository;
    private final LiabilityRepository liabilityRepository;
    private final CurrentUserService currentUserService;

    public NetWorthService(
            AssetRepository assetRepository,
            LiabilityRepository liabilityRepository,
            CurrentUserService currentUserService
    ) {
        this.assetRepository = assetRepository;
        this.liabilityRepository = liabilityRepository;
        this.currentUserService = currentUserService;
    }

    public BigDecimal calculateRecognizedAssets() {

        User user = currentUserService.getCurrentUser();

        List<Asset> assets = assetRepository.findAllByUser(user);

        return assets.stream()
                .map(this::calculateRecognizedAssetValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateRecognizedLiabilities() {

        User user = currentUserService.getCurrentUser();

        List<Liability> liabilities =
                liabilityRepository.findAllByUser(user);

        return liabilities.stream()
                .map(this::calculateRecognizedLiability)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateNetWorth() {

        BigDecimal recognizedAssets =
                calculateRecognizedAssets();

        BigDecimal recognizedLiabilities =
                calculateRecognizedLiabilities();

        return recognizedAssets.subtract(recognizedLiabilities);
    }

    public BigDecimal calculateIncludedAssets() {

        return calculateRecognizedAssets();
    }

    public BigDecimal calculateIncludedLiabilities() {

        return calculateRecognizedLiabilities();
    }

    private BigDecimal calculateRecognizedAssetValue(Asset asset) {

        if (asset.getOwnershipType() == OwnershipType.FAMILY_UNCLEAR) {
            return BigDecimal.ZERO;
        }

        return asset.getTotalValue()
                .multiply(asset.getOwnershipPercentage())
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private BigDecimal calculateRecognizedLiability(
            Liability liability
    ) {

        if (liability.getResponsibilityType()
                == ResponsibilityType.FAMILY_UNCLEAR) {

            return BigDecimal.ZERO;
        }

        return liability.getOutstandingAmount()
                .multiply(liability.getResponsibilityPercentage())
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
    }


}