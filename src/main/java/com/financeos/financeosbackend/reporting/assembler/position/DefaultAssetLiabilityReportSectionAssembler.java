package com.financeos.financeosbackend.reporting.assembler.position;

import com.financeos.financeosbackend.reporting.collector.position.ReportAssetData;
import com.financeos.financeosbackend.reporting.collector.position.ReportLiabilityData;
import com.financeos.financeosbackend.reporting.collector.position.ReportLiabilityItemData;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionMetadata;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import com.financeos.financeosbackend.reporting.dto.v2.AssetAllocationV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.AssetReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportLiabilityItemV2Response;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class DefaultAssetLiabilityReportSectionAssembler
        implements AssetLiabilityReportSectionAssembler {

    @Override
    public AssetReportV2Response assembleAssets(ReportAssetData data) {
        AssetReportV2Response response = new AssetReportV2Response();

        if (data == null) {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "Asset data is unavailable for this period."
                    )
            );
            response.setAllocation(new ArrayList<>());
            return response;
        }

        response.setRecognizedAssets(data.getRecognizedAssets());
        response.setLiquidAssets(data.getLiquidAssets());

        List<AssetAllocationV2Response> allocation = new ArrayList<>();

        BigDecimal totalAssets = data.getRecognizedAssets();

        for (Map.Entry<String, BigDecimal> entry : data.getAllocation().entrySet()) {
            AssetAllocationV2Response item = new AssetAllocationV2Response();

            item.setAssetType(entry.getKey());
            item.setAmount(entry.getValue());

            BigDecimal percentage = BigDecimal.ZERO;

            if (totalAssets != null
                    && totalAssets.compareTo(BigDecimal.ZERO) > 0
                    && entry.getValue() != null) {

                percentage = entry.getValue()
                        .divide(totalAssets, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            item.setPercentage(percentage);
            allocation.add(item);
        }

        allocation.sort(
                Comparator.comparing(
                        AssetAllocationV2Response::getAmount,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                ).reversed()
        );

        response.setAllocation(allocation);

        if (totalAssets == null
                || totalAssets.compareTo(BigDecimal.ZERO) <= 0) {

            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "No recognized assets are available for this period."
                    )
            );
        } else {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.AVAILABLE,
                            "Recognized asset data is available."
                    )
            );
        }

        return response;
    }

    @Override
    public LiabilityReportV2Response assembleLiabilities(
            ReportLiabilityData data) {

        LiabilityReportV2Response response =
                new LiabilityReportV2Response();

        if (data == null) {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "Liability data is unavailable for this period."
                    )
            );
            response.setLiabilities(new ArrayList<>());
            return response;
        }

        response.setRecognizedLiabilities(data.getRecognizedLiabilities());
        response.setDebtPayments(data.getDebtPayments());
        response.setPrincipalPaid(data.getPrincipalPaid());
        response.setInterestPaid(data.getInterestPaid());

        List<ReportLiabilityItemV2Response> liabilities =
                new ArrayList<>();

        for (ReportLiabilityItemData source : data.getLiabilities()) {

            ReportLiabilityItemV2Response item =
                    new ReportLiabilityItemV2Response();

            item.setLiabilityId(source.getLiabilityId());
            item.setLiabilityName(source.getLiabilityName());
            item.setOutstandingAmount(source.getOutstandingAmount());
            item.setMonthlyPayment(source.getMonthlyPayment());
            item.setPrincipalPaid(source.getPrincipalPaid());
            item.setInterestPaid(source.getInterestPaid());
            item.setStatus(source.getStatus());

            liabilities.add(item);
        }

        response.setLiabilities(liabilities);

        BigDecimal recognizedLiabilities =
                data.getRecognizedLiabilities();

        if (recognizedLiabilities == null
                || recognizedLiabilities.compareTo(BigDecimal.ZERO) <= 0) {

            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "No recognized liabilities are available for this period."
                    )
            );
        } else {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.AVAILABLE,
                            "Recognized liability data is available."
                    )
            );
        }

        return response;
    }
}