package com.financeos.financeosbackend.analytics.dto;

import com.financeos.financeosbackend.financialprofile.dto.EmergencyFundContextResponse;
import com.financeos.financeosbackend.financialprofile.dto.FinancialPriorityResponse;
import com.financeos.financeosbackend.financialprofile.dto.FinancialProfileResponse;
import com.financeos.financeosbackend.financialprofile.dto.FinancialResponsibilityContextResponse;
import com.financeos.financeosbackend.financialprofile.dto.IncomeNatureResponse;
import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentResponse;
import com.financeos.financeosbackend.financialprofile.dto.ProtectionContextResponse;

import java.util.List;

public class AnalyticsFinancialProfileV2Response {

    private final FinancialProfileResponse profile;
    private final List<IncomeNatureResponse> incomeNatures;
    private final List<FinancialPriorityResponse> priorities;
    private final InvestmentExperienceAssessmentResponse investmentExperience;
    private final FinancialResponsibilityContextResponse responsibility;
    private final EmergencyFundContextResponse emergencyFund;
    private final ProtectionContextResponse protection;

    public AnalyticsFinancialProfileV2Response(
            FinancialProfileResponse profile,
            List<IncomeNatureResponse> incomeNatures,
            List<FinancialPriorityResponse> priorities,
            InvestmentExperienceAssessmentResponse investmentExperience,
            FinancialResponsibilityContextResponse responsibility,
            EmergencyFundContextResponse emergencyFund,
            ProtectionContextResponse protection
    ) {
        this.profile = profile;
        this.incomeNatures = incomeNatures;
        this.priorities = priorities;
        this.investmentExperience = investmentExperience;
        this.responsibility = responsibility;
        this.emergencyFund = emergencyFund;
        this.protection = protection;
    }

    public FinancialProfileResponse getProfile() {
        return profile;
    }

    public List<IncomeNatureResponse> getIncomeNatures() {
        return incomeNatures;
    }

    public List<FinancialPriorityResponse> getPriorities() {
        return priorities;
    }

    public InvestmentExperienceAssessmentResponse getInvestmentExperience() {
        return investmentExperience;
    }

    public FinancialResponsibilityContextResponse getResponsibility() {
        return responsibility;
    }

    public EmergencyFundContextResponse getEmergencyFund() {
        return emergencyFund;
    }

    public ProtectionContextResponse getProtection() {
        return protection;
    }
}