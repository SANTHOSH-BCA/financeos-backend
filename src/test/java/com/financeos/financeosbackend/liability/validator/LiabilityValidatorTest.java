package com.financeos.financeosbackend.liability.validator;

import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LiabilityValidatorTest {

    private final LiabilityValidator liabilityValidator =
            new LiabilityValidator();

    @Test
    void validateResponsibility_ShouldSet100PercentForIndividual() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setResponsibilityType(
                ResponsibilityType.INDIVIDUAL
        );
        request.setResponsibilityPercentage(null);

        liabilityValidator.validateResponsibility(request);

        assertEquals(
                0,
                BigDecimal.valueOf(100)
                        .compareTo(
                                request.getResponsibilityPercentage()
                        )
        );
    }

    @Test
    void validateResponsibility_ShouldAccept100PercentForIndividual() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setResponsibilityType(
                ResponsibilityType.INDIVIDUAL
        );
        request.setResponsibilityPercentage(
                BigDecimal.valueOf(100)
        );

        assertDoesNotThrow(
                () -> liabilityValidator
                        .validateResponsibility(request)
        );
    }

    @Test
    void validateResponsibility_ShouldRejectNon100PercentIndividual() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setResponsibilityType(
                ResponsibilityType.INDIVIDUAL
        );
        request.setResponsibilityPercentage(
                BigDecimal.valueOf(50)
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> liabilityValidator
                                .validateResponsibility(request)
                );

        assertEquals(
                "Individual responsibility must be 100%",
                exception.getMessage()
        );
    }

    @Test
    void validateResponsibility_ShouldRejectSharedWithoutPercentage() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setResponsibilityType(
                ResponsibilityType.SHARED
        );
        request.setResponsibilityPercentage(null);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> liabilityValidator
                                .validateResponsibility(request)
                );

        assertEquals(
                "Responsibility percentage is required for shared responsibility",
                exception.getMessage()
        );
    }

    @Test
    void validateResponsibility_ShouldAcceptSharedWithPercentage() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setResponsibilityType(
                ResponsibilityType.SHARED
        );
        request.setResponsibilityPercentage(
                BigDecimal.valueOf(70)
        );

        assertDoesNotThrow(
                () -> liabilityValidator
                        .validateResponsibility(request)
        );
    }

    @Test
    void validateResponsibility_ShouldRejectZeroSharedPercentage() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setResponsibilityType(
                ResponsibilityType.SHARED
        );
        request.setResponsibilityPercentage(
                BigDecimal.ZERO
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> liabilityValidator
                                .validateResponsibility(request)
                );

        assertEquals(
                "Responsibility percentage must be between 0 and 100",
                exception.getMessage()
        );
    }

    @Test
    void validateResponsibility_ShouldRejectPercentageAbove100() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setResponsibilityType(
                ResponsibilityType.SHARED
        );
        request.setResponsibilityPercentage(
                BigDecimal.valueOf(101)
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> liabilityValidator
                                .validateResponsibility(request)
                );

        assertEquals(
                "Responsibility percentage must be between 0 and 100",
                exception.getMessage()
        );
    }

    @Test
    void validateResponsibility_ShouldClearPercentageForFamilyUnclear() {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setResponsibilityType(
                ResponsibilityType.FAMILY_UNCLEAR
        );
        request.setResponsibilityPercentage(
                BigDecimal.valueOf(50)
        );

        liabilityValidator.validateResponsibility(request);

        assertNull(
                request.getResponsibilityPercentage()
        );
    }
}