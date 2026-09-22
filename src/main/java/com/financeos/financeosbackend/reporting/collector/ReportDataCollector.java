package com.financeos.financeosbackend.reporting.collector;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.user.entity.User;

public interface ReportDataCollector {

    ReportDataContext collect(
            User user,
            ReportPeriodResponse period
    );
}