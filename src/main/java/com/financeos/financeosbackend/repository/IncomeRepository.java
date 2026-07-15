package com.financeos.financeosbackend.repository;

import com.financeos.financeosbackend.entity.Income;
import com.financeos.financeosbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);

    Optional<Income> findByIdAndUser(Long id, User user);

}