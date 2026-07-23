package com.financeos.financeosbackend.investment.repository;

import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    Page<Investment> findByUser(User user, Pageable pageable);

    Optional<Investment> findByIdAndUser(Long id, User user);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Investment i WHERE i.user = :user")
    BigDecimal getTotalInvestmentByUser(@Param("user") User user);

    @Query("SELECT COUNT(i) FROM Investment i WHERE i.user = :user")
    Long countInvestmentsByUser(@Param("user") User user);

    @Query("""
SELECT i.investmentType, SUM(i.amount)
FROM Investment i
WHERE i.user = :user
GROUP BY i.investmentType
""")
    List<Object[]> getInvestmentDistributionByUser(@Param("user") User user);
}