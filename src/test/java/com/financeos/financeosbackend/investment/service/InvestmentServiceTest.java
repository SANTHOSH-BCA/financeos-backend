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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

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

}