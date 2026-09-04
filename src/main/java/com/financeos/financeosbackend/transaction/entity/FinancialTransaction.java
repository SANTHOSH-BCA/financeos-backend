package com.financeos.financeosbackend.transaction.entity;

import com.financeos.financeosbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.financeos.financeosbackend.transaction.enums.TransactionSource;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;import com.financeos.financeosbackend.expense.entity.Expense;import java.time.LocalDate;import com.financeos.financeosbackend.income.entity.Income;import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime transactionDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionSource source;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(length = 255)
    private String merchantPayee;

    @Column(length = 1000)
    private String description;

    @Column(length = 255)
    private String reference;

    @Column(length = 255)
    private String location;

    @Column
    private LocalDate expectedReturnDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal returnedAmount = BigDecimal.ZERO;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "help_transaction_id")
    private List<FinancialTransaction> helpReturns;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY)
    private Expense expense;

    @OneToOne(mappedBy = "transaction", fetch = FetchType.LAZY)
    private Income income;

    private Long goalId;

    public String getHelpStatus() {

        if (type != TransactionType.HELP_GIVEN
                && type != TransactionType.EXPENSE) {
            return null;
        }

        if (type == TransactionType.EXPENSE) {
            return "CONVERTED_TO_EXPENSE";
        }

        if (status == TransactionStatus.RECONCILED
                && returnedAmount.compareTo(amount) >= 0) {
            return "RETURNED";
        }

        if (returnedAmount.compareTo(BigDecimal.ZERO) > 0
                && returnedAmount.compareTo(amount) < 0) {
            return "PARTIALLY_RETURNED";
        }

        if (status == TransactionStatus.HELP_OVERDUE) {
            return "OVERDUE";
        }

        return "PENDING_RETURN";
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }
}