package com.financeos.financeosbackend.goal.repository;

import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    Page<Goal> findByUser(User user, Pageable pageable);

    Optional<Goal> findByIdAndUser(Long id, User user);

}