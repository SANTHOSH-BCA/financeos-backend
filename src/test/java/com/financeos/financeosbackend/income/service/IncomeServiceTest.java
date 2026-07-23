package com.financeos.financeosbackend.income.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.income.dto.AddIncomeRequest;
import com.financeos.financeosbackend.income.dto.IncomeResponse;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncomeServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private IncomeService incomeService;

    @Test
    void addIncome_ShouldAddIncomeSuccessfully() {

        AddIncomeRequest request = new AddIncomeRequest();
        request.setSource("Salary");
        request.setAmount(new BigDecimal("50000"));
        request.setIncomeDate(LocalDate.now());

        User user = new User();
        user.setId(1L);
        user.setEmail("santhosh@gmail.com");

        Income savedIncome = new Income();
        savedIncome.setSource("Salary");
        savedIncome.setAmount(new BigDecimal("50000"));
        savedIncome.setIncomeDate(LocalDate.now());

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.save(any(Income.class)))
                .thenReturn(savedIncome);

        IncomeResponse response =
                incomeService.addIncome(request);

        assertNotNull(response);
        assertEquals("Salary", response.getSource());
        assertEquals(new BigDecimal("50000"), response.getAmount());
        assertEquals(LocalDate.now(), response.getIncomeDate());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).save(any(Income.class));
    }

    @Test
    void addIncome_ShouldThrowException_WhenIncomeDateIsFuture() {

        AddIncomeRequest request = new AddIncomeRequest();
        request.setSource("Salary");
        request.setAmount(new BigDecimal("50000"));
        request.setIncomeDate(LocalDate.now().plusDays(1));

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> incomeService.addIncome(request));

        assertEquals(
                "Income date cannot be in the future",
                exception.getMessage());

        verify(incomeRepository, never()).save(any());
    }

    @Test
    void getMyIncome_ShouldReturnIncomeList() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setSource("Salary");
        income.setAmount(new BigDecimal("50000"));
        income.setIncomeDate(LocalDate.now());

        Page<Income> page =
                new PageImpl<>(List.of(income));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(
                eq(user),
                any(PageRequest.class)))
                .thenReturn(page);

        Page<IncomeResponse> response =
                incomeService.getMyIncome(PageRequest.of(0, 5));

        assertEquals(1, response.getTotalElements());

        IncomeResponse first =
                response.getContent().get(0);

        assertEquals("Salary", first.getSource());
        assertEquals(new BigDecimal("50000"), first.getAmount());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository)
                .findByUser(eq(user), any(PageRequest.class));
    }

    @Test
    void updateIncome_ShouldUpdateSuccessfully() {

        AddIncomeRequest request = new AddIncomeRequest();
        request.setSource("Freelancing");
        request.setAmount(new BigDecimal("75000"));
        request.setIncomeDate(LocalDate.now());

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setSource("Salary");
        income.setAmount(new BigDecimal("50000"));
        income.setIncomeDate(LocalDate.now().minusDays(1));
        income.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(income));

        when(incomeRepository.save(any(Income.class)))
                .thenReturn(income);

        IncomeResponse response =
                incomeService.updateIncome(1L, request);

        assertNotNull(response);
        assertEquals("Freelancing", response.getSource());
        assertEquals(new BigDecimal("75000"), response.getAmount());
        assertEquals(LocalDate.now(), response.getIncomeDate());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByIdAndUser(1L, user);
        verify(incomeRepository).save(any(Income.class));
    }

    @Test
    void updateIncome_ShouldThrowException_WhenIncomeNotFound() {

        AddIncomeRequest request = new AddIncomeRequest();
        request.setSource("Salary");
        request.setAmount(new BigDecimal("50000"));
        request.setIncomeDate(LocalDate.now());

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> incomeService.updateIncome(1L, request));

        assertEquals("Income not found", exception.getMessage());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByIdAndUser(1L, user);
        verify(incomeRepository, never()).save(any());
    }

    @Test
    void deleteIncome_ShouldDeleteSuccessfully() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setSource("Salary");
        income.setAmount(new BigDecimal("50000"));
        income.setIncomeDate(LocalDate.now());
        income.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(income));

        incomeService.deleteIncome(1L);

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByIdAndUser(1L, user);
        verify(incomeRepository).delete(income);
    }

    @Test
    void deleteIncome_ShouldThrowException_WhenIncomeNotFound() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> incomeService.deleteIncome(1L));

        assertEquals("Income not found", exception.getMessage());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByIdAndUser(1L, user);
        verify(incomeRepository, never()).delete(any(Income.class));
    }

}
