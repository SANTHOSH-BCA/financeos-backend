package com.financeos.financeosbackend.reporting.collector.position;

import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.enums.LiabilityStatus;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetLiabilityReportDataCollectorTest {

    @Mock
    private NetWorthService netWorthService;

    @Mock
    private LiabilityRepository liabilityRepository;

    @Mock
    private LiabilityRepaymentRepository liabilityRepaymentRepository;

    @InjectMocks
    private AssetLiabilityReportDataCollector collector;

    @Test
    void shouldCollectAssetData() {
        when(netWorthService.calculateRecognizedAssets())
                .thenReturn(new BigDecimal("100000"));

        when(netWorthService.calculateRecognizedLiquidAssets())
                .thenReturn(new BigDecimal("40000"));

        when(netWorthService.calculateRecognizedAssetAllocation())
                .thenReturn(Map.of(
                        AssetType.CASH, new BigDecimal("10000"),
                        AssetType.BANK_ACCOUNT, new BigDecimal("30000"),
                        AssetType.GOLD, new BigDecimal("60000")
                ));

        ReportAssetData result = collector.collectAssets();

        assertNotNull(result);
        assertEquals(new BigDecimal("100000"), result.getRecognizedAssets());
        assertEquals(new BigDecimal("40000"), result.getLiquidAssets());

        assertEquals(
                new BigDecimal("10000"),
                result.getAllocation().get("CASH")
        );

        assertEquals(
                new BigDecimal("30000"),
                result.getAllocation().get("BANK_ACCOUNT")
        );

        assertEquals(
                new BigDecimal("60000"),
                result.getAllocation().get("GOLD")
        );
    }

    @Test
    void shouldCollectLiabilityData() {
        User user = new User();

        ReportPeriodResponse period = new ReportPeriodResponse(
                ReportPeriodType.MONTHLY,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 31)
        );

        Liability liability = new Liability();
        liability.setId(1L);
        liability.setUser(user);
        liability.setLiabilityName("Home Loan");
        liability.setOutstandingAmount(new BigDecimal("500000"));
        liability.setPaymentAmount(new BigDecimal("15000"));
        liability.setStatus(LiabilityStatus.ACTIVE);

        LiabilityRepayment repayment = new LiabilityRepayment();
        repayment.setLiability(liability);
        repayment.setPaymentAmount(new BigDecimal("15000"));
        repayment.setPrincipalAmount(new BigDecimal("10000"));
        repayment.setInterestAmount(new BigDecimal("5000"));
        repayment.setRepaymentDate(LocalDate.of(2026, 8, 10));

        when(netWorthService.calculateRecognizedLiabilities())
                .thenReturn(new BigDecimal("500000"));

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        when(liabilityRepaymentRepository
                .findAllByLiabilityAndRepaymentDateBetweenOrderByRepaymentDateDesc(
                        liability,
                        period.getStartDate(),
                        period.getEndDate()
                ))
                .thenReturn(List.of(repayment));

        ReportLiabilityData result =
                collector.collectLiabilities(user, period);

        assertNotNull(result);

        assertEquals(
                new BigDecimal("500000"),
                result.getRecognizedLiabilities()
        );

        assertEquals(
                new BigDecimal("15000"),
                result.getDebtPayments()
        );

        assertEquals(
                new BigDecimal("10000"),
                result.getPrincipalPaid()
        );

        assertEquals(
                new BigDecimal("5000"),
                result.getInterestPaid()
        );

        assertEquals(1, result.getLiabilities().size());

        ReportLiabilityItemData item =
                result.getLiabilities().get(0);

        assertEquals(1L, item.getLiabilityId());
        assertEquals("Home Loan", item.getLiabilityName());
        assertEquals(
                new BigDecimal("500000"),
                item.getOutstandingAmount()
        );
        assertEquals(
                new BigDecimal("15000"),
                item.getMonthlyPayment()
        );
        assertEquals(
                new BigDecimal("10000"),
                item.getPrincipalPaid()
        );
        assertEquals(
                new BigDecimal("5000"),
                item.getInterestPaid()
        );
        assertEquals(
                LiabilityStatus.ACTIVE.name(),
                item.getStatus()
        );
    }

    @Test
    void shouldReturnEmptyLiabilityDataWhenUserHasNoLiabilities() {
        User user = new User();

        ReportPeriodResponse period = new ReportPeriodResponse(
                ReportPeriodType.MONTHLY,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 31)
        );

        when(netWorthService.calculateRecognizedLiabilities())
                .thenReturn(BigDecimal.ZERO);

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of());

        ReportLiabilityData result =
                collector.collectLiabilities(user, period);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getRecognizedLiabilities());
        assertEquals(BigDecimal.ZERO, result.getDebtPayments());
        assertEquals(BigDecimal.ZERO, result.getPrincipalPaid());
        assertEquals(BigDecimal.ZERO, result.getInterestPaid());
        assertTrue(result.getLiabilities().isEmpty());
    }

    @Test
    void shouldRejectNullUser() {
        ReportPeriodResponse period = new ReportPeriodResponse(
                ReportPeriodType.MONTHLY,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 31)
        );

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> collector.collectLiabilities(null, period)
        );
    }

    @Test
    void shouldRejectNullPeriod() {
        User user = new User();

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> collector.collectLiabilities(user, null)
        );
    }
}