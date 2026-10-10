package com.financeos.financeosbackend.user.repository;

import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.id > :lastId ORDER BY u.id ASC")
    List<Long> findNextUserIds(
            @Param("lastId") Long lastId,
            Pageable pageable
    );
}