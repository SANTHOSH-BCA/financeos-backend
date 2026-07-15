package com.financeos.financeosbackend.repository;

import com.financeos.financeosbackend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.financeos.financeosbackend.entity.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    java.util.Optional<Expense> findByIdAndUser(Long id, User user);

}