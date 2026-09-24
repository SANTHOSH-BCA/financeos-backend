package com.financeos.financeosbackend.notification.entity;

import com.financeos.financeosbackend.notification.enums.NotificationPriority;
import com.financeos.financeosbackend.notification.enums.NotificationSource;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.enums.NotificationType;
import com.financeos.financeosbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(
                        name = "idx_notification_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_notification_user_status",
                        columnList = "user_id, status"
                ),
                @Index(
                        name = "idx_notification_created_at",
                        columnList = "created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // Ownership
    // =========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // =========================
    // Classification
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status;

    // =========================
    // Content
    // =========================

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    // =========================
    // Source
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(name = "source_module", nullable = false, length = 50)
    private NotificationSource sourceModule;

    @Column(name = "source_event_id", length = 100)
    private String sourceEventId;

    // =========================
    // Related Financial Entity
    // =========================

    @Column(name = "related_entity_type", length = 50)
    private String relatedEntityType;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    // =========================
    // Action
    // =========================

    @Column(name = "action_type", length = 50)
    private String actionType;

    @Column(name = "action_target", length = 255)
    private String actionTarget;

    // =========================
    // Deduplication
    // =========================

    @Column(name = "deduplication_key", length = 255)
    private String deduplicationKey;

    // =========================
    // Lifecycle
    // =========================

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "actioned_at")
    private LocalDateTime actionedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}