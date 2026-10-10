package com.financeos.financeosbackend.notification.integration.insurance;

import com.financeos.financeosbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InsuranceReminderScheduler {

    private static final int BATCH_SIZE = 100;

    private final UserRepository userRepository;
    private final InsuranceReminderProcessor insuranceReminderProcessor;

    @Scheduled(cron = "${financeos.insurance.reminders.cron:0 0 0 * * *}")
    public void processDailyInsuranceReminders() {
        log.info("Starting daily insurance reminder processing.");

        long lastProcessedUserId = 0L;
        int usersProcessed = 0;
        int usersFailed = 0;
        int notificationsCreated = 0;
        int notificationsSkipped = 0;

        while (true) {
            List<Long> userIds = userRepository.findNextUserIds(
                    lastProcessedUserId,
                    PageRequest.of(0, BATCH_SIZE)
            );

            if (userIds.isEmpty()) {
                break;
            }

            for (Long userId : userIds) {
                lastProcessedUserId = userId;

                try {
                    InsuranceReminderProcessingResult result =
                            insuranceReminderProcessor.processRemindersForUser(userId);

                    usersProcessed++;
                    notificationsCreated += result.notificationsCreated();
                    notificationsSkipped += result.notificationsSkipped();

                } catch (Exception exception) {
                    usersFailed++;

                    log.error(
                            "Insurance reminder processing failed for user ID {}.",
                            userId,
                            exception
                    );
                }
            }

            if (userIds.size() < BATCH_SIZE) {
                break;
            }
        }

        log.info(
                "Daily insurance reminder processing completed. Users processed: {}, " +
                        "users failed: {}, notifications created: {}, notifications skipped: {}.",
                usersProcessed,
                usersFailed,
                notificationsCreated,
                notificationsSkipped
        );
    }
}