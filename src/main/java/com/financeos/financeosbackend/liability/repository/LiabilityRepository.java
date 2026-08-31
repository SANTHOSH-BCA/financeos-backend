package com.financeos.financeosbackend.liability.repository;

import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LiabilityRepository extends JpaRepository<Liability, Long> {

    List<Liability> findAllByUser(User user);

    Optional<Liability> findByIdAndUser(Long id, User user);
}