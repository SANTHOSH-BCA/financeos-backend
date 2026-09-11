package com.financeos.financeosbackend.liability.event;

import com.financeos.financeosbackend.liability.enums.LiabilityStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LiabilityNotificationEvent {

    public enum EventType {
        UPCOMING_PAYMENT,
        PAYMENT_OVERDUE,
        REPAYMENT_MILESTONE,
        LOAN_NEARING_COMPLETION
    }

    private final Long userId;
    private final Long liabilityId;
    private final String liabilityName;
    private final EventType eventType;
    private final LiabilityStatus liabilityStatus;
    private final BigDecimal outstandingAmount;
    private final BigDecimal paymentAmount;
    private final LocalDate nextPaymentDate;
    private final String message;
    private final LocalDateTime occurredAt;

    public LiabilityNotificationEvent(
            Long userId,
            Long liabilityId,
            String liabilityName,
            EventType eventType,
            LiabilityStatus liabilityStatus,
            BigDecimal outstandingAmount,
            BigDecimal paymentAmount,
            LocalDate nextPaymentDate,
            String message
    ) {
        this.userId = userId;
        this.liabilityId = liabilityId;
        this.liabilityName = liabilityName;
        this.eventType = eventType;
        this.liabilityStatus = liabilityStatus;
        this.outstandingAmount = outstandingAmount;
        this.paymentAmount = paymentAmount;
        this.nextPaymentDate = nextPaymentDate;
        this.message = message;
        this.occurredAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public Long getLiabilityId() {
        return liabilityId;
    }

    public String getLiabilityName() {
        return liabilityName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public LiabilityStatus getLiabilityStatus() {
        return liabilityStatus;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}