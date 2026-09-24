package com.financeos.financeosbackend.notification.management;

import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.repository.NotificationRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationManagementServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationManagementService service;

    @Test
    void shouldMarkNotificationAsRead() {

        User user = new User();
        user.setId(1L);

        Notification notification =
                new Notification();

        notification.setId(10L);
        notification.setUser(user);
        notification.setStatus(
                NotificationStatus.UNREAD
        );

        when(notificationRepository.findById(10L))
                .thenReturn(Optional.of(notification));

        NotificationManagementResult result =
                service.markAsRead(1L, 10L);

        assertTrue(result.success());

        assertEquals(
                NotificationStatus.READ,
                notification.getStatus()
        );

        assertNotNull(
                notification.getReadAt()
        );

        verify(notificationRepository)
                .save(notification);
    }

    @Test
    void shouldRejectAnotherUsersNotification() {

        User user = new User();
        user.setId(2L);

        Notification notification =
                new Notification();

        notification.setId(10L);
        notification.setUser(user);

        when(notificationRepository.findById(10L))
                .thenReturn(Optional.of(notification));

        assertThrows(
                NotificationNotFoundException.class,
                () -> service.markAsRead(1L, 10L)
        );

        verify(notificationRepository, never())
                .save(any());
    }
}