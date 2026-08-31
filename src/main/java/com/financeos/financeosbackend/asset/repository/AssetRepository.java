package com.financeos.financeosbackend.asset.repository;

import com.financeos.financeosbackend.asset.entity.Asset;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findAllByUser(User user);

    Optional<Asset> findByIdAndUser(Long id, User user);

}