package com.financeos.financeosbackend.liability.mapper;

import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.enums.LiabilityType;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LiabilityMapperTest {

    private final LiabilityMapper liabilityMapper =
            new LiabilityMapper();

    @Test
    void toEntity_ShouldMapCreateRequestToLiability() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setLiabilityName("Home Loan");
        request.setLiabilityType(LiabilityType.HOME_LOAN);
        request.setOutstandingAmount(
                new BigDecimal("2000000")
        );
        request.setResponsibilityType(
                ResponsibilityType.SHARED
        );
        request.setResponsibilityPercentage(
                new BigDecimal("70")
        );
        request.setValuationDate(
                LocalDate.of(2026, 8, 31)
        );

        Liability liability =
                liabilityMapper.toEntity(request);

        assertNotNull(liability);
        assertEquals(
                "Home Loan",
                liability.getLiabilityName()
        );
        assertEquals(
                LiabilityType.HOME_LOAN,
                liability.getLiabilityType()
        );
        assertEquals(
                new BigDecimal("2000000"),
                liability.getOutstandingAmount()
        );
        assertEquals(
                ResponsibilityType.SHARED,
                liability.getResponsibilityType()
        );
        assertEquals(
                new BigDecimal("70"),
                liability.getResponsibilityPercentage()
        );
        assertEquals(
                LocalDate.of(2026, 8, 31),
                liability.getValuationDate()
        );
    }

    @Test
    void toResponse_ShouldCalculateFullLiabilityForIndividualResponsibility() {

        Liability liability = createLiability(
                "Personal Loan",
                new BigDecimal("500000"),
                ResponsibilityType.INDIVIDUAL,
                new BigDecimal("100")
        );

        LiabilityResponse response =
                liabilityMapper.toResponse(liability);

        assertEquals(
                0,
                new BigDecimal("500000.00")
                        .compareTo(response.getRecognizedLiability())
        );

        assertTrue(response.isIncludedInNetWorth());
    }

    @Test
    void toResponse_ShouldCalculateResponsibilityShareForSharedLiability() {

        Liability liability = createLiability(
                "Home Loan",
                new BigDecimal("2000000"),
                ResponsibilityType.SHARED,
                new BigDecimal("70")
        );

        LiabilityResponse response =
                liabilityMapper.toResponse(liability);

        assertEquals(
                0,
                new BigDecimal("1400000.00")
                        .compareTo(response.getRecognizedLiability())
        );

        assertTrue(response.isIncludedInNetWorth());
    }

    @Test
    void toResponse_ShouldExcludeFamilyUnclearLiability() {

        Liability liability = createLiability(
                "Family Home Loan",
                new BigDecimal("1500000"),
                ResponsibilityType.FAMILY_UNCLEAR,
                null
        );

        LiabilityResponse response =
                liabilityMapper.toResponse(liability);

        assertEquals(
                BigDecimal.ZERO,
                response.getRecognizedLiability()
        );

        assertFalse(response.isIncludedInNetWorth());

        assertNull(
                response.getResponsibilityPercentage()
        );
    }

    private Liability createLiability(
            String name,
            BigDecimal outstandingAmount,
            ResponsibilityType responsibilityType,
            BigDecimal responsibilityPercentage
    ) {

        Liability liability = new Liability();

        liability.setLiabilityName(name);
        liability.setLiabilityType(
                LiabilityType.OTHER
        );
        liability.setOutstandingAmount(
                outstandingAmount
        );
        liability.setResponsibilityType(
                responsibilityType
        );
        liability.setResponsibilityPercentage(
                responsibilityPercentage
        );
        liability.setValuationDate(
                LocalDate.now()
        );

        return liability;
    }
}