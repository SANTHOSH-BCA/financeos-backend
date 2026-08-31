package com.financeos.financeosbackend.asset.validator;

import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import org.springframework.stereotype.Component;

@Component
public class AssetValidator {

    public void validateOwnership(CreateAssetRequest request) {

        OwnershipType ownershipType = request.getOwnershipType();

        if (ownershipType == OwnershipType.INDIVIDUAL) {

            if (request.getOwnershipPercentage() == null) {
                request.setOwnershipPercentage(
                        java.math.BigDecimal.valueOf(100)
                );
            }

            if (request.getOwnershipPercentage()
                    .compareTo(java.math.BigDecimal.valueOf(100)) != 0) {

                throw new IllegalArgumentException(
                        "Individual ownership must be 100%"
                );
            }
        }

        if (ownershipType == OwnershipType.SHARED
                && request.getOwnershipPercentage() == null) {

            throw new IllegalArgumentException(
                    "Ownership percentage is required for shared ownership"
            );
        }

        if (ownershipType == OwnershipType.FAMILY_UNCLEAR) {

            request.setOwnershipPercentage(null);
        }
    }
}