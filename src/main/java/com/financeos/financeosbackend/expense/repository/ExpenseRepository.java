package com.financeos.financeosbackend.expense.repository;

import com.financeos.financeosbackend.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.financeos.financeosbackend.user.entity.User;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
    Page<Expense> findByUser(User user, Pageable pageable);
    java.util.Optional<Expense> findByIdAndUser(Long id, User user);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user")
    BigDecimal getTotalExpenseByUser(@Param("user") User user);

    @Query("SELECT COUNT(e) FROM Expense e WHERE e.user = :user")
    Long countExpensesByUser(@Param("user") User user);

}