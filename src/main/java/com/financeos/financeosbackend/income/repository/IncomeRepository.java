package com.financeos.financeosbackend.income.repository;

import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financeos.financeosbackend.analytics.dto.MonthlySummary;
public interface IncomeRepository extends JpaRepository<Income, Long> {

    Page<Income> findByUser(User user, Pageable pageable);

    List<Income> findByUser(User user);

    Optional<Income> findByIdAndUser(Long id, User user);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.user = :user")
    BigDecimal getTotalIncomeByUser(@Param("user") User user);

    @Query("SELECT COUNT(i) FROM Income i WHERE i.user = :user")
    Long countIncomeByUser(@Param("user") User user);



}