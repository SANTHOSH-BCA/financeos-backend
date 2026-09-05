package com.financeos.financeosbackend.investment.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;
import com.financeos.financeosbackend.investment.dto.InvestmentResponse;
import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;import com.financeos.financeosbackend.networth.service.NetWorthService;import com.financeos.financeosbackend.investment.dto.InvestmentPerformanceResponse;import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private NetWorthService netWorthService;

    @InjectMocks
    private InvestmentService investmentService;

    @Test
    void addInvestment_ShouldAddInvestmentSuccessfully() {

        AddInvestmentRequest request = new AddInvestmentRequest();
        request.setInvestmentName("Mutual Fund");
        request.setInvestmentType("SIP");
        request.setAmount(new BigDecimal("5000"));
        request.setInvestmentDate(LocalDate.now());

        User user = new User();
        user.setId(1L);
        user.setEmail("santhosh@gmail.com");

        Investment investment = new Investment();
        investment.setInvestmentName("Mutual Fund");
        investment.setInvestmentType("SIP");
        investment.setAmount(new BigDecimal("5000"));
        investment.setInvestmentDate(LocalDate.now());

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.save(any(Investment.class)))
                .thenReturn(investment);

        InvestmentResponse response =
                investmentService.addInvestment(request);

        assertNotNull(response);
        assertEquals("Mutual Fund", response.getInvestmentName());
        assertEquals("SIP", response.getInvestmentType());
        assertEquals(new BigDecimal("5000"), response.getAmount());
        assertEquals(LocalDate.now(), response.getInvestmentDate());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).save(any(Investment.class));
    }

    @Test
    void addInvestment_ShouldThrowException_WhenInvestmentDateIsFuture() {

        AddInvestmentRequest request = new AddInvestmentRequest();
        request.setInvestmentName("Mutual Fund");
        request.setInvestmentType("SIP");
        request.setAmount(new BigDecimal("5000"));
        request.setInvestmentDate(LocalDate.now().plusDays(1));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> investmentService.addInvestment(request));

        assertEquals(
                "Investment date cannot be in the future",
                exception.getMessage());

        verify(investmentRepository, never()).save(any());
    }

    @Test
    void getMyInvestments_ShouldReturnInvestments() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Investment investment = new Investment();
        investment.setInvestmentName("Mutual Fund");
        investment.setInvestmentType("SIP");
        investment.setAmount(new BigDecimal("5000"));
        investment.setInvestmentDate(LocalDate.now());

        Page<Investment> page =
                new PageImpl<>(List.of(investment));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.findByUser(
                eq(user),
                any(PageRequest.class)))
                .thenReturn(page);

        Page<InvestmentResponse> response =
                investmentService.getMyInvestments(PageRequest.of(0,5));

        assertEquals(1, response.getTotalElements());

        InvestmentResponse first =
                response.getContent().get(0);

        assertEquals("Mutual Fund", first.getInvestmentName());
        assertEquals("SIP", first.getInvestmentType());
        assertEquals(new BigDecimal("5000"), first.getAmount());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository)
                .findByUser(eq(user), any(PageRequest.class));
    }

    @Test
    void updateInvestment_ShouldUpdateSuccessfully() {

        AddInvestmentRequest request = new AddInvestmentRequest();
        request.setInvestmentName("Stocks");
        request.setInvestmentType("Equity");
        request.setAmount(new BigDecimal("25000"));
        request.setInvestmentDate(LocalDate.now());

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Investment investment = new Investment();
        investment.setInvestmentName("Mutual Fund");
        investment.setInvestmentType("SIP");
        investment.setAmount(new BigDecimal("5000"));
        investment.setInvestmentDate(LocalDate.now());
        investment.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(investment));

        when(investmentRepository.save(any(Investment.class)))
                .thenReturn(investment);

        InvestmentResponse response =
                investmentService.updateInvestment(1L, request);

        assertNotNull(response);
        assertEquals("Stocks", response.getInvestmentName());
        assertEquals("Equity", response.getInvestmentType());
        assertEquals(new BigDecimal("25000"), response.getAmount());
        assertEquals(LocalDate.now(), response.getInvestmentDate());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).findByIdAndUser(1L, user);
        verify(investmentRepository).save(any(Investment.class));
    }

    @Test
    void updateInvestment_ShouldThrowException_WhenInvestmentNotFound() {

        AddInvestmentRequest request = new AddInvestmentRequest();
        request.setInvestmentName("Stocks");
        request.setInvestmentType("Equity");
        request.setAmount(new BigDecimal("25000"));
        request.setInvestmentDate(LocalDate.now());

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> investmentService.updateInvestment(1L, request));

        assertEquals("Investment not found", exception.getMessage());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).findByIdAndUser(1L, user);
        verify(investmentRepository, never()).save(any());
    }

    @Test
    void deleteInvestment_ShouldDeleteSuccessfully() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Investment investment = new Investment();
        investment.setInvestmentName("Mutual Fund");
        investment.setInvestmentType("SIP");
        investment.setAmount(new BigDecimal("5000"));
        investment.setInvestmentDate(LocalDate.now());
        investment.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(investment));

        investmentService.deleteInvestment(1L);

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).findByIdAndUser(1L, user);
        verify(investmentRepository).delete(investment);
    }

    @Test
    void deleteInvestment_ShouldThrowException_WhenInvestmentNotFound() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> investmentService.deleteInvestment(1L));

        assertEquals("Investment not found", exception.getMessage());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).findByIdAndUser(1L, user);
        verify(investmentRepository, never()).delete(any(Investment.class));
    }

    @Test
    void getPortfolioPerformance_ShouldCalculatePortfolioMetrics() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("100000"));

        when(investmentRepository.getTotalCurrentValueByUser(user))
                .thenReturn(new BigDecimal("115000"));

        when(investmentRepository.getTotalProfitLossByUser(user))
                .thenReturn(new BigDecimal("15000"));

        when(investmentRepository.countInvestmentsByUser(user))
                .thenReturn(4L);

        InvestmentPerformanceResponse response =
                investmentService.getPortfolioPerformance();

        assertNotNull(response);

        assertEquals(
                new BigDecimal("100000"),
                response.getTotalInvestedAmount()
        );

        assertEquals(
                new BigDecimal("115000"),
                response.getCurrentPortfolioValue()
        );

        assertEquals(
                new BigDecimal("15000"),
                response.getTotalProfitLoss()
        );

        assertEquals(
                new BigDecimal("15.000000"),
                response.getReturnPercentage()
        );

        assertEquals(
                4L,
                response.getInvestmentCount()
        );

        verify(investmentRepository).getTotalInvestmentByUser(user);
        verify(investmentRepository).getTotalCurrentValueByUser(user);
        verify(investmentRepository).getTotalProfitLossByUser(user);
        verify(investmentRepository).countInvestmentsByUser(user);
    }

    @Test
    void getAssetAllocation_ShouldCalculateAllocationByInvestmentType() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getInvestmentDistributionByUser(user))
                .thenReturn(List.of(
                        new Object[]{
                                "Mutual Fund",
                                new BigDecimal("60000")
                        },
                        new Object[]{
                                "Stocks",
                                new BigDecimal("40000")
                        }
                ));

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("100000"));

        var response = investmentService.getAssetAllocation();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(
                "Mutual Fund",
                response.get(0).getInvestmentType()
        );

        assertEquals(
                new BigDecimal("60000"),
                response.get(0).getInvestedAmount()
        );

        assertEquals(
                new BigDecimal("60.000000"),
                response.get(0).getAllocationPercentage()
        );

        assertEquals(
                "Stocks",
                response.get(1).getInvestmentType()
        );

        assertEquals(
                new BigDecimal("40000"),
                response.get(1).getInvestedAmount()
        );

        assertEquals(
                new BigDecimal("40.000000"),
                response.get(1).getAllocationPercentage()
        );
    }

    @Test
    void getInvestmentExposure_ShouldCalculateExposureByInvestmentType() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Investment mutualFund = new Investment();
        mutualFund.setInvestmentType("Mutual Fund");
        mutualFund.setTotalInvestedAmount(new BigDecimal("60000"));
        mutualFund.setCurrentValue(new BigDecimal("70000"));

        Investment stocks = new Investment();
        stocks.setInvestmentType("Stocks");
        stocks.setTotalInvestedAmount(new BigDecimal("40000"));
        stocks.setCurrentValue(new BigDecimal("30000"));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.findByUser(
                eq(user),
                any(Pageable.class)
        )).thenReturn(
                new PageImpl<>(
                        List.of(mutualFund, stocks)
                )
        );

        var response = investmentService.getInvestmentExposure();

        assertNotNull(response);
        assertEquals(2, response.size());

        var mutualFundExposure = response.stream()
                .filter(item ->
                        "Mutual Fund".equals(item.getInvestmentType()))
                .findFirst()
                .orElseThrow();

        var stocksExposure = response.stream()
                .filter(item ->
                        "Stocks".equals(item.getInvestmentType()))
                .findFirst()
                .orElseThrow();

        assertEquals(
                new BigDecimal("70000"),
                mutualFundExposure.getCurrentValue()
        );

        assertEquals(
                new BigDecimal("70.00"),
                mutualFundExposure.getExposurePercentage()
        );

        assertEquals(
                new BigDecimal("30000"),
                stocksExposure.getCurrentValue()
        );

        assertEquals(
                new BigDecimal("30.00"),
                stocksExposure.getExposurePercentage()
        );
    }

    @Test
    void calculateInvestmentToNetWorthPercentage_ShouldCalculateCorrectly() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalCurrentValueByUser(user))
                .thenReturn(new BigDecimal("60000"));

        when(netWorthService.calculateNetWorth())
                .thenReturn(new BigDecimal("200000"));

        BigDecimal result =
                investmentService.calculateInvestmentToNetWorthPercentage();

        assertEquals(
                new BigDecimal("30.00"),
                result
        );

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository)
                .getTotalCurrentValueByUser(user);
        verify(netWorthService).calculateNetWorth();
    }

    @Test
    void calculateInvestmentToNetWorthPercentage_ShouldReturnZero_WhenNetWorthIsZero() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalCurrentValueByUser(user))
                .thenReturn(new BigDecimal("60000"));

        when(netWorthService.calculateNetWorth())
                .thenReturn(BigDecimal.ZERO);

        BigDecimal result =
                investmentService.calculateInvestmentToNetWorthPercentage();

        assertEquals(
                BigDecimal.ZERO,
                result
        );

        verify(netWorthService).calculateNetWorth();
    }

}