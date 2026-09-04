package com.financeos.financeosbackend.networth.service;

import com.financeos.financeosbackend.asset.entity.Asset;
import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import com.financeos.financeosbackend.asset.repository.AssetRepository;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.enums.LiabilityType;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NetWorthServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private LiabilityRepository liabilityRepository;

    @Mock
    private CurrentUserService currentUserService;

    private NetWorthService netWorthService;

    private User user;

    @BeforeEach
    void setUp() {

        netWorthService = new NetWorthService(
                assetRepository,
                liabilityRepository,
                currentUserService
        );

        user = new User();
    }

    @Test
    void calculateRecognizedAssets_ShouldSumIndividualAndSharedAssets() {

        Asset individualAsset = createAsset(
                "Personal Gold",
                new BigDecimal("200000"),
                OwnershipType.INDIVIDUAL,
                new BigDecimal("100")
        );

        Asset sharedAsset = createAsset(
                "Family Gold",
                new BigDecimal("200000"),
                OwnershipType.SHARED,
                new BigDecimal("25")
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(assetRepository.findAllByUser(user))
                .thenReturn(List.of(individualAsset, sharedAsset));

        BigDecimal result =
                netWorthService.calculateRecognizedAssets();

        assertEquals(
                0,
                new BigDecimal("250000.00").compareTo(result)
        );
    }

    @Test
    void calculateRecognizedAssets_ShouldExcludeFamilyUnclearAssets() {

        Asset unclearAsset = createAsset(
                "Family Property",
                new BigDecimal("5000000"),
                OwnershipType.FAMILY_UNCLEAR,
                null
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(assetRepository.findAllByUser(user))
                .thenReturn(List.of(unclearAsset));

        BigDecimal result =
                netWorthService.calculateRecognizedAssets();

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(result)
        );
    }

    @Test
    void calculateRecognizedLiabilities_ShouldSumIndividualAndSharedLiabilities() {

        Liability individualLiability = createLiability(
                "Home Loan",
                new BigDecimal("1000000"),
                ResponsibilityType.INDIVIDUAL,
                new BigDecimal("100")
        );

        Liability sharedLiability = createLiability(
                "Joint Loan",
                new BigDecimal("500000"),
                ResponsibilityType.SHARED,
                new BigDecimal("50")
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(
                        individualLiability,
                        sharedLiability
                ));

        BigDecimal result =
                netWorthService.calculateRecognizedLiabilities();

        assertEquals(
                0,
                new BigDecimal("1250000.00").compareTo(result)
        );
    }

    @Test
    void calculateRecognizedLiabilities_ShouldExcludeFamilyUnclearLiabilities() {

        Liability unclearLiability = createLiability(
                "Family Loan",
                new BigDecimal("800000"),
                ResponsibilityType.FAMILY_UNCLEAR,
                null
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(unclearLiability));

        BigDecimal result =
                netWorthService.calculateRecognizedLiabilities();

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(result)
        );
    }

    @Test
    void calculateNetWorth_ShouldSubtractRecognizedLiabilitiesFromRecognizedAssets() {

        Asset asset = createAsset(
                "Personal Gold",
                new BigDecimal("500000"),
                OwnershipType.INDIVIDUAL,
                new BigDecimal("100")
        );

        Liability liability = createLiability(
                "Personal Loan",
                new BigDecimal("150000"),
                ResponsibilityType.INDIVIDUAL,
                new BigDecimal("100")
        );

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(assetRepository.findAllByUser(user))
                .thenReturn(List.of(asset));
        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        BigDecimal result =
                netWorthService.calculateNetWorth();

        assertEquals(
                0,
                new BigDecimal("350000.00").compareTo(result)
        );
    }

    @Test
    void calculateNetWorth_ShouldReturnZeroWhenNoAssetsAndNoLiabilities() {

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(assetRepository.findAllByUser(user))
                .thenReturn(List.of());
        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of());

        BigDecimal result =
                netWorthService.calculateNetWorth();

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(result)
        );
    }

    private Asset createAsset(
            String name,
            BigDecimal value,
            OwnershipType ownershipType,
            BigDecimal ownershipPercentage
    ) {

        Asset asset = new Asset();

        asset.setUser(user);
        asset.setAssetName(name);
        asset.setAssetType(AssetType.OTHER);
        asset.setTotalValue(value);
        asset.setOwnershipType(ownershipType);
        asset.setOwnershipPercentage(ownershipPercentage);
        asset.setValuationDate(LocalDate.now());

        return asset;
    }

    private Liability createLiability(
            String name,
            BigDecimal amount,
            ResponsibilityType responsibilityType,
            BigDecimal responsibilityPercentage
    ) {

        Liability liability = new Liability();

        liability.setUser(user);
        liability.setLiabilityName(name);
        liability.setLiabilityType(LiabilityType.OTHER);
        liability.setOutstandingAmount(amount);
        liability.setResponsibilityType(responsibilityType);
        liability.setResponsibilityPercentage(responsibilityPercentage);
        liability.setValuationDate(LocalDate.now());

        return liability;
    }
}