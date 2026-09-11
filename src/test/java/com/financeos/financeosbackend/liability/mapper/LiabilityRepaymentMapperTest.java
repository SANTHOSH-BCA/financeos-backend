package com.financeos.financeosbackend.liability.mapper;

import com.financeos.financeosbackend.liability.dto.CreateLiabilityRepaymentRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityRepaymentResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.enums.LiabilityType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LiabilityRepaymentMapperTest {

    private final LiabilityRepaymentMapper mapper =
            new LiabilityRepaymentMapper();

    @Test
    void shouldMapRequestToEntity() {
        Liability liability = new Liability();
        liability.setLiabilityName("Home Loan");
        liability.setLiabilityType(LiabilityType.HOME_LOAN);

        CreateLiabilityRepaymentRequest request =
                new CreateLiabilityRepaymentRequest();

        request.setPaymentAmount(new BigDecimal("25000"));
        request.setPrincipalAmount(new BigDecimal("18000"));
        request.setInterestAmount(new BigDecimal("7000"));
        request.setRepaymentDate(LocalDate.of(2026, 9, 1));
        request.setNotes("September EMI");

        LiabilityRepayment repayment =
                mapper.toEntity(request, liability);

        assertSame(liability, repayment.getLiability());
        assertEquals(
                new BigDecimal("25000"),
                repayment.getPaymentAmount()
        );
        assertEquals(
                new BigDecimal("18000"),
                repayment.getPrincipalAmount()
        );
        assertEquals(
                new BigDecimal("7000"),
                repayment.getInterestAmount()
        );
        assertEquals(
                LocalDate.of(2026, 9, 1),
                repayment.getRepaymentDate()
        );
        assertEquals("September EMI", repayment.getNotes());
    }

    @Test
    void shouldMapEntityToResponse() {
        Liability liability = new Liability();
        liability.setId(10L);

        LiabilityRepayment repayment = new LiabilityRepayment();
        repayment.setId(20L);
        repayment.setLiability(liability);
        repayment.setPaymentAmount(new BigDecimal("25000"));
        repayment.setPrincipalAmount(new BigDecimal("18000"));
        repayment.setInterestAmount(new BigDecimal("7000"));
        repayment.setRepaymentDate(LocalDate.of(2026, 9, 1));
        repayment.setNotes("September EMI");

        LiabilityRepaymentResponse response =
                mapper.toResponse(repayment);

        assertEquals(20L, response.getId());
        assertEquals(10L, response.getLiabilityId());
        assertEquals(
                new BigDecimal("25000"),
                response.getPaymentAmount()
        );
        assertEquals(
                new BigDecimal("18000"),
                response.getPrincipalAmount()
        );
        assertEquals(
                new BigDecimal("7000"),
                response.getInterestAmount()
        );
        assertEquals(
                LocalDate.of(2026, 9, 1),
                response.getRepaymentDate()
        );
        assertEquals("September EMI", response.getNotes());
    }
}