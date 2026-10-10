package com.financeos.financeosbackend.notification.integration.insurance;

import com.financeos.financeosbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsuranceReminderSchedulerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InsuranceReminderProcessor insuranceReminderProcessor;

    @InjectMocks
    private InsuranceReminderScheduler scheduler;

    @Test
    void shouldProcessUsersAndStopAfterPartialBatch() {
        when(userRepository.findNextUserIds(eq(0L), any(Pageable.class)))
                .thenReturn(List.of(10L, 20L));

        when(insuranceReminderProcessor.processRemindersForUser(10L))
                .thenReturn(new InsuranceReminderProcessingResult(3, 2, 1));
        when(insuranceReminderProcessor.processRemindersForUser(20L))
                .thenReturn(new InsuranceReminderProcessingResult(2, 1, 1));

        assertDoesNotThrow(() -> scheduler.processDailyInsuranceReminders());

        verify(insuranceReminderProcessor).processRemindersForUser(10L);
        verify(insuranceReminderProcessor).processRemindersForUser(20L);
        verify(userRepository, times(1))
                .findNextUserIds(eq(0L), any(Pageable.class));
    }

    @Test
    void shouldContinueProcessingWhenOneUserFails() {
        when(userRepository.findNextUserIds(eq(0L), any(Pageable.class)))
                .thenReturn(List.of(10L, 20L));

        when(insuranceReminderProcessor.processRemindersForUser(10L))
                .thenThrow(new IllegalStateException("Simulated processing failure"));
        when(insuranceReminderProcessor.processRemindersForUser(20L))
                .thenReturn(new InsuranceReminderProcessingResult(1, 1, 0));

        assertDoesNotThrow(() -> scheduler.processDailyInsuranceReminders());

        verify(insuranceReminderProcessor).processRemindersForUser(10L);
        verify(insuranceReminderProcessor).processRemindersForUser(20L);
    }

    @Test
    void shouldStopWhenNoUsersAreFound() {
        when(userRepository.findNextUserIds(eq(0L), any(Pageable.class)))
                .thenReturn(List.of());

        assertDoesNotThrow(() -> scheduler.processDailyInsuranceReminders());

        verify(insuranceReminderProcessor, never()).processRemindersForUser(anyLong());
        verify(userRepository, times(1))
                .findNextUserIds(eq(0L), any(Pageable.class));
    }

    @Test
    void shouldContinueWithNextBatchWhenCurrentBatchIsFull() {
        List<Long> firstBatch = LongStream.rangeClosed(1, 100)
                .boxed()
                .toList();

        when(userRepository.findNextUserIds(eq(0L), any(Pageable.class)))
                .thenReturn(firstBatch);
        when(userRepository.findNextUserIds(eq(100L), any(Pageable.class)))
                .thenReturn(List.of(101L));

        when(insuranceReminderProcessor.processRemindersForUser(anyLong()))
                .thenReturn(new InsuranceReminderProcessingResult(0, 0, 0));

        assertDoesNotThrow(() -> scheduler.processDailyInsuranceReminders());

        verify(insuranceReminderProcessor, times(101))
                .processRemindersForUser(anyLong());
        verify(userRepository, times(1))
                .findNextUserIds(eq(100L), any(Pageable.class));
        verify(userRepository, times(1))
                .findNextUserIds(eq(0L), any(Pageable.class));
    }
}