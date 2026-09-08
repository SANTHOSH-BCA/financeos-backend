package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.FinancialPriorityRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialPriorityResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialPriority;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.repository.FinancialPriorityRepository;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
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
class FinancialProfilePriorityServiceTest {

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private FinancialProfileRepository financialProfileRepository;

    @Mock
    private FinancialPriorityRepository priorityRepository;

    @InjectMocks
    private FinancialProfilePriorityService service;

    @Test
    void shouldSaveAndRankPriorities() {

        User user = new User();
        FinancialProfile profile = new FinancialProfile();

        FinancialPriorityRequest first = new FinancialPriorityRequest();
        first.setPriority("Emergency Fund");
        first.setPriorityRank(1);

        FinancialPriorityRequest second = new FinancialPriorityRequest();
        second.setPriority("Debt Reduction");
        second.setPriorityRank(2);

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(financialProfileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));

        when(priorityRepository
                .findByFinancialProfileOrderByPriorityRankAsc(profile))
                .thenReturn(List.of());

        FinancialPriority priority1 =
                new FinancialPriority(profile, "Emergency Fund", 1);

        FinancialPriority priority2 =
                new FinancialPriority(profile, "Debt Reduction", 2);

        when(priorityRepository.saveAll(anyList()))
                .thenReturn(List.of(priority1, priority2));

        List<FinancialPriorityResponse> result =
                service.savePriorities(List.of(first, second));

        assertEquals(2, result.size());
        assertEquals("Emergency Fund", result.get(0).getPriority());
        assertEquals(1, result.get(0).getPriorityRank());
        assertEquals("Debt Reduction", result.get(1).getPriority());
        assertEquals(2, result.get(1).getPriorityRank());

        verify(priorityRepository).deleteAll(anyList());
        verify(priorityRepository).saveAll(anyList());
    }
}