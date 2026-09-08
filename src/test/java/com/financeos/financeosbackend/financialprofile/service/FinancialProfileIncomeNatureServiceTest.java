package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.IncomeNatureRequest;
import com.financeos.financeosbackend.financialprofile.dto.IncomeNatureResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.IncomeNature;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import com.financeos.financeosbackend.financialprofile.repository.IncomeNatureRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialProfileIncomeNatureServiceTest {

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private FinancialProfileRepository financialProfileRepository;

    @Mock
    private IncomeNatureRepository incomeNatureRepository;

    @InjectMocks
    private FinancialProfileIncomeNatureService service;

    @Test
    void shouldSaveMultipleIncomeNatures() {

        User user = new User();
        FinancialProfile profile = new FinancialProfile();

        IncomeNatureRequest salary = new IncomeNatureRequest();
        salary.setIncomeNature("Salary");

        IncomeNatureRequest rental = new IncomeNatureRequest();
        rental.setIncomeNature("Rental Income");

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(financialProfileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));

        when(incomeNatureRepository.findByFinancialProfile(profile))
                .thenReturn(List.of());

        IncomeNature savedSalary = new IncomeNature(profile, "Salary");
        IncomeNature savedRental = new IncomeNature(profile, "Rental Income");

        when(incomeNatureRepository.saveAll(anyList()))
                .thenReturn(List.of(savedSalary, savedRental));

        List<IncomeNatureResponse> result =
                service.saveIncomeNatures(List.of(salary, rental));

        assertEquals(2, result.size());
        assertEquals("Salary", result.get(0).getIncomeNature());
        assertEquals("Rental Income", result.get(1).getIncomeNature());

        verify(incomeNatureRepository).saveAll(anyList());
    }

    @Test
    void shouldReturnIncomeNaturesForCurrentUser() {

        User user = new User();
        FinancialProfile profile = new FinancialProfile();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(financialProfileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));

        when(incomeNatureRepository.findByFinancialProfile(profile))
                .thenReturn(List.of(
                        new IncomeNature(profile, "Salary"),
                        new IncomeNature(profile, "Freelance Income")
                ));

        List<IncomeNatureResponse> result =
                service.getIncomeNatures();

        assertEquals(2, result.size());
        assertEquals("Salary", result.get(0).getIncomeNature());
        assertEquals(
                "Freelance Income",
                result.get(1).getIncomeNature()
        );
    }
}