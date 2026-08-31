package com.financeos.financeosbackend.asset.mapper;

import com.financeos.financeosbackend.asset.dto.AssetResponse;
import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.entity.Asset;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AssetMapper {

    public Asset toEntity(CreateAssetRequest request) {

        Asset asset = new Asset();

        asset.setAssetName(request.getAssetName());
        asset.setAssetType(request.getAssetType());
        asset.setTotalValue(request.getTotalValue());
        asset.setOwnershipType(request.getOwnershipType());
        asset.setOwnershipPercentage(request.getOwnershipPercentage());
        asset.setValuationDate(request.getValuationDate());

        return asset;
    }

    public AssetResponse toResponse(Asset asset) {

        AssetResponse response = new AssetResponse();

        response.setId(asset.getId());
        response.setAssetName(asset.getAssetName());
        response.setAssetType(asset.getAssetType());
        response.setTotalValue(asset.getTotalValue());
        response.setOwnershipType(asset.getOwnershipType());
        response.setOwnershipPercentage(asset.getOwnershipPercentage());
        response.setValuationDate(asset.getValuationDate());
        response.setCreatedAt(asset.getCreatedAt());
        response.setUpdatedAt(asset.getUpdatedAt());

        BigDecimal recognizedValue = calculateRecognizedValue(asset);

        response.setRecognizedValue(recognizedValue);
        response.setIncludedInNetWorth(recognizedValue.compareTo(BigDecimal.ZERO) > 0);

        return response;
    }

    private BigDecimal calculateRecognizedValue(Asset asset) {

        if (asset.getOwnershipType()
                == com.financeos.financeosbackend.asset.enums.OwnershipType.FAMILY_UNCLEAR) {

            return BigDecimal.ZERO;
        }

        return asset.getTotalValue()
                .multiply(asset.getOwnershipPercentage())
                .divide(BigDecimal.valueOf(100));
    }
}