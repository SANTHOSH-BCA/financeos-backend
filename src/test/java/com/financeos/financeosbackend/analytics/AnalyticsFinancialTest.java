package com.financeos.financeosbackend.analytics;

import com.financeos.financeosbackend.analytics.dto.CashFlowResponse;
import com.financeos.financeosbackend.analytics.dto.ExpenseCategoryResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlyFinancialSummaryResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlyIncomeExpenseResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlySavingsResponse;
import com.financeos.financeosbackend.analytics.dto.NetWorthResponse;
import com.financeos.financeosbackend.analytics.service.AnalyticsService;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.financeos.financeosbackend.financialposition.service.FinancialPositionService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import com.financeos.financeosbackend.investment.service.InvestmentService;
import com.financeos.financeosbackend.investment.service.InvestmentInsightService;
import com.financeos.financeosbackend.goalintelligence.service.GoalIntelligenceService;
import com.financeos.financeosbackend.financialhealth.service.FinancialHealthService;
import com.financeos.financeosbackend.financialprofile.service.FinancialProfileService;
import com.financeos.financeosbackend.financialprofile.service.FinancialProfileIncomeNatureService;
import com.financeos.financeosbackend.financialprofile.service.FinancialProfilePriorityService;
import com.financeos.financeosbackend.financialprofile.service.InvestmentExperienceAssessmentService;
import com.financeos.financeosbackend.financialprofile.service.FinancialResponsibilityContextService;
import com.financeos.financeosbackend.financialprofile.service.EmergencyFundContextService;
import com.financeos.financeosbackend.financialprofile.service.ProtectionContextService;
import com.financeos.financeosbackend.analytics.dto.AnalyticsUnifiedV2Response;
import com.financeos.financeosbackend.analytics.dto.AnalyticsFinancialProfileV2Response;
@ExtendWith(MockitoExtension.class)
class AnalyticsFinancialTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private CashFlowService cashFlowService;

    @Mock
    private FinancialPositionService financialPositionService;

    @Mock
    private NetWorthService netWorthService;

    @Mock
    private InvestmentService investmentService;

    @Mock
    private InvestmentInsightService investmentInsightService;

    @Mock
    private GoalIntelligenceService goalIntelligenceService;

    @Mock
    private FinancialHealthService financialHealthService;

    @Mock
    private FinancialProfileService financialProfileService;

    @Mock
    private FinancialProfileIncomeNatureService financialProfileIncomeNatureService;

    @Mock
    private FinancialProfilePriorityService financialProfilePriorityService;

    @Mock
    private InvestmentExperienceAssessmentService investmentExperienceAssessmentService;

    @Mock
    private FinancialResponsibilityContextService financialResponsibilityContextService;

    @Mock
    private EmergencyFundContextService emergencyFundContextService;

    @Mock
    private ProtectionContextService protectionContextService;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getMonthlyIncomeExpense_ShouldReturnMonthlySummary() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setAmount(new BigDecimal("50000"));
        income.setIncomeDate(LocalDate.of(2026, 7, 10));

        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("20000"));
        expense.setExpenseDate(LocalDate.of(2026, 7, 15));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(user))
                .thenReturn(List.of(income));

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        List<MonthlyIncomeExpenseResponse> response =
                analyticsService.getMonthlyIncomeExpense();

        assertEquals(1, response.size());

        MonthlyIncomeExpenseResponse month = response.get(0);

        assertEquals("2026-07", month.getMonth());
        assertEquals(new BigDecimal("50000"), month.getTotalIncome());
        assertEquals(new BigDecimal("20000"), month.getTotalExpense());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByUser(user);
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getExpenseByCategory_ShouldReturnCategoryTotals() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Expense e1 = new Expense();
        e1.setCategory("Food");
        e1.setAmount(new BigDecimal("500"));

        Expense e2 = new Expense();
        e2.setCategory("Food");
        e2.setAmount(new BigDecimal("1000"));

        Expense e3 = new Expense();
        e3.setCategory("Travel");
        e3.setAmount(new BigDecimal("2000"));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(e1, e2, e3));

        List<ExpenseCategoryResponse> response =
                analyticsService.getExpenseByCategory();

        assertEquals(2, response.size());

        ExpenseCategoryResponse food = response.stream()
                .filter(r -> r.getCategory().equals("Food"))
                .findFirst()
                .orElseThrow();

        ExpenseCategoryResponse travel = response.stream()
                .filter(r -> r.getCategory().equals("Travel"))
                .findFirst()
                .orElseThrow();

        assertEquals(new BigDecimal("1500"), food.getTotalAmount());
        assertEquals(new BigDecimal("2000"), travel.getTotalAmount());

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getMonthlySavings_ShouldReturnMonthlySavings() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setAmount(new BigDecimal("60000"));
        income.setIncomeDate(LocalDate.of(2026, 7, 5));

        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("25000"));
        expense.setExpenseDate(LocalDate.of(2026, 7, 10));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(user))
                .thenReturn(List.of(income));

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        List<MonthlySavingsResponse> response =
                analyticsService.getMonthlySavings();

        assertEquals(1, response.size());

        MonthlySavingsResponse summary = response.get(0);

        assertEquals("2026-07", summary.getMonth());
        assertEquals(new BigDecimal("60000"), summary.getIncome());
        assertEquals(new BigDecimal("25000"), summary.getExpense());
        assertEquals(new BigDecimal("35000"), summary.getSavings());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByUser(user);
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getCashFlow_ShouldReturnCashFlow() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("80000"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("30000"));

        CashFlowResponse response =
                analyticsService.getCashFlow();

        assertNotNull(response);
        assertEquals(new BigDecimal("80000"), response.getTotalIncome());
        assertEquals(new BigDecimal("30000"), response.getTotalExpense());
        assertEquals(new BigDecimal("50000"), response.getNetCashFlow());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).getTotalIncomeByUser(user);
        verify(expenseRepository).getTotalExpenseByUser(user);
    }

    @Test
    void getNetWorth_ShouldReturnNetWorth() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("450000"));

        NetWorthResponse response =
                analyticsService.getNetWorth();

        assertNotNull(response);
        assertEquals(new BigDecimal("450000"), response.getTotalAssets());
        assertEquals(BigDecimal.ZERO, response.getTotalLiabilities());
        assertEquals(new BigDecimal("450000"), response.getNetWorth());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).getTotalInvestmentByUser(user);
    }

    @Test
    void getMonthlyFinancialSummary_ShouldReturnSummary() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("100000"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("70000"));

        MonthlyFinancialSummaryResponse response =
                analyticsService.getMonthlyFinancialSummary();

        assertNotNull(response);

        assertEquals(
                new BigDecimal("100000"),
                response.getIncome());

        assertEquals(
                new BigDecimal("70000"),
                response.getExpense());

        assertEquals(
                new BigDecimal("30000"),
                response.getSavings());

        assertEquals(
                new BigDecimal("30.00"),
                response.getSavingsRate());

        assertEquals(
                "EXCELLENT",
                response.getFinancialHealth());

        assertTrue(
                response.getSummary().contains("Excellent"));

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).getTotalIncomeByUser(user);
        verify(expenseRepository).getTotalExpenseByUser(user);
    }

    @Test
    void getUnifiedV2_ShouldReturnUnifiedAnalytics() {

        when(financialPositionService.calculateNetWorth())
                .thenReturn(BigDecimal.ZERO);

        when(financialPositionService.calculateNetCashFlow())
                .thenReturn(new BigDecimal("47200"));

        when(financialPositionService.calculateInvestmentValue())
                .thenReturn(new BigDecimal("36000"));

        when(financialPositionService.calculateDebtValue())
                .thenReturn(BigDecimal.ZERO);

        when(financialPositionService.calculateLiquidAssets())
                .thenReturn(BigDecimal.ZERO);

        when(financialPositionService.calculateAssetAllocation())
                .thenReturn(java.util.Map.of());

        when(financialPositionService.calculateSavings())
                .thenReturn(new BigDecimal("47200"));

        when(financialPositionService.calculateSavingsRate())
                .thenReturn(new BigDecimal("85.82"));

        when(cashFlowService.calculateIncludedInflows())
                .thenReturn(new BigDecimal("55000"));

        when(cashFlowService.calculateIncludedOutflows())
                .thenReturn(new BigDecimal("7800"));

        when(cashFlowService.calculateNetCashFlow())
                .thenReturn(new BigDecimal("47200"));

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("47200"));

        when(cashFlowService.calculateSavingsRate())
                .thenReturn(new BigDecimal("85.82"));

        when(netWorthService.calculateIncludedAssets())
                .thenReturn(BigDecimal.ZERO);

        when(netWorthService.calculateIncludedLiabilities())
                .thenReturn(BigDecimal.ZERO);

        when(netWorthService.calculateNetWorth())
                .thenReturn(BigDecimal.ZERO);

        when(investmentService.calculateInvestmentToNetWorthPercentage())
                .thenReturn(BigDecimal.ZERO);

        when(goalIntelligenceService.getMyGoals())
                .thenReturn(List.of());

        when(financialProfileService.getMyProfile())
                .thenReturn(null);

        when(financialProfileIncomeNatureService.getIncomeNatures())
                .thenReturn(List.of());

        when(financialProfilePriorityService.getPriorities())
                .thenReturn(List.of());

        when(investmentExperienceAssessmentService.getAssessment())
                .thenReturn(null);

        when(financialResponsibilityContextService.get())
                .thenReturn(null);

        when(emergencyFundContextService.get())
                .thenReturn(null);

        when(protectionContextService.get())
                .thenReturn(null);

        AnalyticsUnifiedV2Response response =
                analyticsService.getUnifiedV2();

        assertNotNull(response);
        assertNotNull(response.getFinancialPosition());
        assertNotNull(response.getCashFlow());
        assertNotNull(response.getNetWorth());
        assertNotNull(response.getInvestments());
        assertNotNull(response.getGoals());
        assertNotNull(response.getFinancialHealth());
        assertNotNull(response.getFinancialProfile());

        verify(cashFlowService).calculateNetCashFlow();
        verify(netWorthService).calculateNetWorth();
        verify(financialPositionService).calculateNetWorth();
    }

}