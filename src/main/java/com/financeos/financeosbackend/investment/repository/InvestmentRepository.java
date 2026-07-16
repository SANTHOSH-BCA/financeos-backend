package com.financeos.financeosbackend.investment.repository;

import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    Page<Investment> findByUser(User user, Pageable pageable);

    Optional<Investment> findByIdAndUser(Long id, User user);

}