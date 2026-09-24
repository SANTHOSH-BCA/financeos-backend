package com.financeos.financeosbackend.notification.preference;

import com.financeos.financeosbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "notification_preferences",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_notification_preference_user",
                        columnNames = "user_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "investment_updates", nullable = false)
    private boolean investmentUpdates = true;

    @Column(name = "goal_alerts", nullable = false)
    private boolean goalAlerts = true;

    @Column(name = "expense_alerts", nullable = false)
    private boolean expenseAlerts = true;

    @Column(name = "help_reminders", nullable = false)
    private boolean helpReminders = true;

    @Column(name = "insurance_reminders", nullable = false)
    private boolean insuranceReminders = true;

    @Column(name = "daily_financial_update", nullable = false)
    private boolean dailyFinancialUpdate = true;
}