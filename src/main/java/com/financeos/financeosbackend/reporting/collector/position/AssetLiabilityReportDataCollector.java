package com.financeos.financeosbackend.reporting.collector.position;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AssetLiabilityReportDataCollector {

    private final NetWorthService netWorthService;
    private final LiabilityRepository liabilityRepository;
    private final LiabilityRepaymentRepository liabilityRepaymentRepository;
    private final CurrentUserService currentUserService;

    public AssetLiabilityReportDataCollector(
            NetWorthService netWorthService,
            LiabilityRepository liabilityRepository,
            LiabilityRepaymentRepository liabilityRepaymentRepository,
            CurrentUserService currentUserService) {

        this.netWorthService = netWorthService;
        this.liabilityRepository = liabilityRepository;
        this.liabilityRepaymentRepository = liabilityRepaymentRepository;
        this.currentUserService = currentUserService;
    }

    public ReportAssetData collectAssets() {

        ReportAssetData data = new ReportAssetData();

        data.setRecognizedAssets(
                netWorthService.calculateRecognizedAssets()
        );

        data.setLiquidAssets(
                netWorthService.calculateRecognizedLiquidAssets()
        );

        Map<?, BigDecimal> allocation =
                netWorthService.calculateRecognizedAssetAllocation();

        data.setAllocation(
                allocation.entrySet()
                        .stream()
                        .collect(
                                java.util.stream.Collectors.toMap(
                                        entry -> entry.getKey().toString(),
                                        Map.Entry::getValue
                                )
                        )
        );

        return data;
    }

    public ReportLiabilityData collectLiabilities(
            User user,
            ReportPeriodResponse period) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User must not be null"
            );
        }

        if (period == null) {
            throw new IllegalArgumentException(
                    "Report period must not be null"
            );
        }

        ReportLiabilityData data =
                new ReportLiabilityData();

        data.setRecognizedLiabilities(
                netWorthService.calculateRecognizedLiabilities()
        );

        List<Liability> liabilities =
                liabilityRepository.findAllByUser(user);

        List<ReportLiabilityItemData> items =
                new ArrayList<>();

        BigDecimal debtPayments = BigDecimal.ZERO;
        BigDecimal principalPaid = BigDecimal.ZERO;
        BigDecimal interestPaid = BigDecimal.ZERO;

        for (Liability liability : liabilities) {

            List<LiabilityRepayment> repayments =
                    liabilityRepaymentRepository
                            .findAllByLiabilityAndRepaymentDateBetweenOrderByRepaymentDateDesc(
                                    liability,
                                    period.getStartDate(),
                                    period.getEndDate()
                            );

            BigDecimal liabilityPrincipal = BigDecimal.ZERO;
            BigDecimal liabilityInterest = BigDecimal.ZERO;
            BigDecimal liabilityPayments = BigDecimal.ZERO;

            for (LiabilityRepayment repayment : repayments) {

                BigDecimal payment =
                        repayment.getPaymentAmount() != null
                                ? repayment.getPaymentAmount()
                                : BigDecimal.ZERO;

                BigDecimal principal =
                        repayment.getPrincipalAmount() != null
                                ? repayment.getPrincipalAmount()
                                : BigDecimal.ZERO;

                BigDecimal interest =
                        repayment.getInterestAmount() != null
                                ? repayment.getInterestAmount()
                                : BigDecimal.ZERO;

                liabilityPayments =
                        liabilityPayments.add(payment);

                liabilityPrincipal =
                        liabilityPrincipal.add(principal);

                liabilityInterest =
                        liabilityInterest.add(interest);
            }

            debtPayments =
                    debtPayments.add(liabilityPayments);

            principalPaid =
                    principalPaid.add(liabilityPrincipal);

            interestPaid =
                    interestPaid.add(liabilityInterest);

            ReportLiabilityItemData item =
                    new ReportLiabilityItemData();

            item.setLiabilityId(liability.getId());
            item.setLiabilityName(liability.getLiabilityName());
            item.setOutstandingAmount(
                    liability.getOutstandingAmount()
            );
            item.setMonthlyPayment(
                    liability.getPaymentAmount()
            );
            item.setPrincipalPaid(liabilityPrincipal);
            item.setInterestPaid(liabilityInterest);

            item.setStatus(
                    liability.getStatus() == null
                            ? null
                            : liability.getStatus().name()
            );

            items.add(item);
        }

        data.setDebtPayments(debtPayments);
        data.setPrincipalPaid(principalPaid);
        data.setInterestPaid(interestPaid);
        data.setLiabilities(items);

        return data;
    }
}