package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.enums.LiabilityType;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import com.financeos.financeosbackend.liability.mapper.LiabilityMapper;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.liability.validator.LiabilityValidator;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiabilityServiceTest {

    @Mock
    private LiabilityRepository liabilityRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private LiabilityMapper liabilityMapper;

    @Mock
    private LiabilityValidator liabilityValidator;

    @InjectMocks
    private LiabilityService liabilityService;

    @Test
    void createLiability_ShouldCreateSuccessfully() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        CreateLiabilityRequest request = new CreateLiabilityRequest();

        request.setLiabilityName("Home Loan");
        request.setLiabilityType(LiabilityType.HOME_LOAN);
        request.setOutstandingAmount(new BigDecimal("2000000"));
        request.setResponsibilityType(ResponsibilityType.SHARED);
        request.setResponsibilityPercentage(new BigDecimal("70"));
        request.setValuationDate(LocalDate.now());

        Liability liability = new Liability();
        Liability savedLiability = new Liability();

        LiabilityResponse response = new LiabilityResponse();
        response.setId(1L);
        response.setLiabilityName("Home Loan");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityMapper.toEntity(request))
                .thenReturn(liability);

        when(liabilityRepository.save(liability))
                .thenReturn(savedLiability);

        when(liabilityMapper.toResponse(savedLiability))
                .thenReturn(response);

        LiabilityResponse result =
                liabilityService.createLiability(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Home Loan", result.getLiabilityName());

        verify(liabilityValidator).validateResponsibility(request);
        verify(currentUserService).getCurrentUser();
        verify(liabilityMapper).toEntity(request);
        verify(liabilityRepository).save(liability);
        verify(liabilityMapper).toResponse(savedLiability);
    }

    @Test
    void getMyLiabilities_ShouldReturnCurrentUsersLiabilities() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Liability liability = new Liability();

        LiabilityResponse response = new LiabilityResponse();
        response.setId(1L);
        response.setLiabilityName("Personal Loan");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        when(liabilityMapper.toResponse(liability))
                .thenReturn(response);

        List<LiabilityResponse> result =
                liabilityService.getMyLiabilities();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(
                "Personal Loan",
                result.get(0).getLiabilityName()
        );

        verify(currentUserService).getCurrentUser();
        verify(liabilityRepository).findAllByUser(user);
        verify(liabilityMapper).toResponse(liability);
    }

    @Test
    void getMyLiability_ShouldReturnCurrentUsersLiability() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Liability liability = new Liability();

        LiabilityResponse response = new LiabilityResponse();
        response.setId(1L);
        response.setLiabilityName("Vehicle Loan");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(liability));

        when(liabilityMapper.toResponse(liability))
                .thenReturn(response);

        LiabilityResponse result =
                liabilityService.getMyLiability(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(
                "Vehicle Loan",
                result.getLiabilityName()
        );

        verify(currentUserService).getCurrentUser();
        verify(liabilityRepository).findByIdAndUser(1L, user);
        verify(liabilityMapper).toResponse(liability);
    }

    @Test
    void getMyLiability_ShouldThrowException_WhenLiabilityDoesNotExist() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findByIdAndUser(999L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> liabilityService.getMyLiability(999L)
                );

        assertEquals(
                "Liability not found",
                exception.getMessage()
        );

        verify(currentUserService).getCurrentUser();
        verify(liabilityRepository)
                .findByIdAndUser(999L, user);

        verify(liabilityMapper, never())
                .toResponse(any());
    }
}