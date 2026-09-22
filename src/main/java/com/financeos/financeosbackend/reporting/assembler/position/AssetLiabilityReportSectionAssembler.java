package com.financeos.financeosbackend.reporting.assembler.position;

import com.financeos.financeosbackend.reporting.collector.position.ReportAssetData;
import com.financeos.financeosbackend.reporting.collector.position.ReportLiabilityData;
import com.financeos.financeosbackend.reporting.dto.v2.AssetReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;

public interface AssetLiabilityReportSectionAssembler {

    AssetReportV2Response assembleAssets(ReportAssetData data);

    LiabilityReportV2Response assembleLiabilities(ReportLiabilityData data);
}