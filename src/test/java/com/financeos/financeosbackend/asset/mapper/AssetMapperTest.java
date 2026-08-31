package com.financeos.financeosbackend.asset.mapper;

import com.financeos.financeosbackend.asset.dto.AssetResponse;
import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.entity.Asset;
import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AssetMapperTest {

    private final AssetMapper assetMapper = new AssetMapper();

    @Test
    void toEntity_ShouldMapCreateRequestToAsset() {

        CreateAssetRequest request = new CreateAssetRequest();

        request.setAssetName("Family Gold");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(new BigDecimal("200000"));
        request.setOwnershipType(OwnershipType.SHARED);
        request.setOwnershipPercentage(new BigDecimal("25"));
        request.setValuationDate(LocalDate.of(2026, 8, 31));

        Asset asset = assetMapper.toEntity(request);

        assertNotNull(asset);
        assertEquals("Family Gold", asset.getAssetName());
        assertEquals(AssetType.GOLD, asset.getAssetType());
        assertEquals(
                new BigDecimal("200000"),
                asset.getTotalValue()
        );
        assertEquals(
                OwnershipType.SHARED,
                asset.getOwnershipType()
        );
        assertEquals(
                new BigDecimal("25"),
                asset.getOwnershipPercentage()
        );
        assertEquals(
                LocalDate.of(2026, 8, 31),
                asset.getValuationDate()
        );
    }

    @Test
    void toResponse_ShouldCalculateFullValueForIndividualOwnership() {

        Asset asset = createAsset(
                "Personal Gold",
                new BigDecimal("200000"),
                OwnershipType.INDIVIDUAL,
                new BigDecimal("100")
        );

        AssetResponse response = assetMapper.toResponse(asset);

        assertEquals(
                0,
                new BigDecimal("200000.00")
                        .compareTo(response.getRecognizedValue())
        );

        assertTrue(response.isIncludedInNetWorth());
    }

    @Test
    void toResponse_ShouldCalculateOwnershipShareForSharedAsset() {

        Asset asset = createAsset(
                "Family Gold",
                new BigDecimal("200000"),
                OwnershipType.SHARED,
                new BigDecimal("25")
        );

        AssetResponse response = assetMapper.toResponse(asset);

        assertEquals(
                0,
                new BigDecimal("50000.00")
                        .compareTo(response.getRecognizedValue())
        );

        assertTrue(response.isIncludedInNetWorth());
    }

    @Test
    void toResponse_ShouldExcludeFamilyUnclearAssetFromNetWorth() {

        Asset asset = createAsset(
                "Family Property",
                new BigDecimal("5000000"),
                OwnershipType.FAMILY_UNCLEAR,
                null
        );

        AssetResponse response = assetMapper.toResponse(asset);

        assertEquals(
                BigDecimal.ZERO,
                response.getRecognizedValue()
        );

        assertFalse(response.isIncludedInNetWorth());
        assertNull(response.getOwnershipPercentage());
    }

    private Asset createAsset(
            String name,
            BigDecimal value,
            OwnershipType ownershipType,
            BigDecimal ownershipPercentage
    ) {

        Asset asset = new Asset();

        asset.setAssetName(name);
        asset.setAssetType(AssetType.OTHER);
        asset.setTotalValue(value);
        asset.setOwnershipType(ownershipType);
        asset.setOwnershipPercentage(ownershipPercentage);
        asset.setValuationDate(LocalDate.now());

        return asset;
    }
}