package com.financeos.financeosbackend.asset.validator;

import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AssetValidatorTest {

    private final AssetValidator assetValidator = new AssetValidator();

    @Test
    void validateOwnership_ShouldSet100PercentForIndividualOwnership() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(null);

        assetValidator.validateOwnership(request);

        assertEquals(
                0,
                BigDecimal.valueOf(100)
                        .compareTo(request.getOwnershipPercentage())
        );
    }

    @Test
    void validateOwnership_ShouldAccept100PercentForIndividualOwnership() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(
                BigDecimal.valueOf(100)
        );

        assertDoesNotThrow(
                () -> assetValidator.validateOwnership(request)
        );
    }

    @Test
    void validateOwnership_ShouldRejectNon100PercentIndividualOwnership() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(
                BigDecimal.valueOf(50)
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> assetValidator.validateOwnership(request)
                );

        assertEquals(
                "Individual ownership must be 100%",
                exception.getMessage()
        );
    }

    @Test
    void validateOwnership_ShouldRejectSharedOwnershipWithoutPercentage() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setOwnershipType(OwnershipType.SHARED);
        request.setOwnershipPercentage(null);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> assetValidator.validateOwnership(request)
                );

        assertEquals(
                "Ownership percentage is required for shared ownership",
                exception.getMessage()
        );
    }

    @Test
    void validateOwnership_ShouldAcceptSharedOwnershipWithPercentage() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setOwnershipType(OwnershipType.SHARED);
        request.setOwnershipPercentage(
                BigDecimal.valueOf(25)
        );

        assertDoesNotThrow(
                () -> assetValidator.validateOwnership(request)
        );
    }

    @Test
    void validateOwnership_ShouldClearPercentageForFamilyUnclear() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setOwnershipType(OwnershipType.FAMILY_UNCLEAR);
        request.setOwnershipPercentage(
                BigDecimal.valueOf(25)
        );

        assetValidator.validateOwnership(request);

        assertNull(request.getOwnershipPercentage());
    }
}