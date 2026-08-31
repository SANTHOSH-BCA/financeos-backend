package com.financeos.financeosbackend.asset.service;

import com.financeos.financeosbackend.asset.dto.AssetResponse;
import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.entity.Asset;
import com.financeos.financeosbackend.asset.mapper.AssetMapper;
import com.financeos.financeosbackend.asset.repository.AssetRepository;
import com.financeos.financeosbackend.asset.validator.AssetValidator;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final CurrentUserService currentUserService;
    private final AssetMapper assetMapper;
    private final AssetValidator assetValidator;

    public AssetService(
            AssetRepository assetRepository,
            CurrentUserService currentUserService,
            AssetMapper assetMapper,
            AssetValidator assetValidator
    ) {
        this.assetRepository = assetRepository;
        this.currentUserService = currentUserService;
        this.assetMapper = assetMapper;
        this.assetValidator = assetValidator;
    }

    public AssetResponse createAsset(CreateAssetRequest request) {

        assetValidator.validateOwnership(request);

        User user = currentUserService.getCurrentUser();

        Asset asset = assetMapper.toEntity(request);
        asset.setUser(user);

        Asset savedAsset = assetRepository.save(asset);

        return assetMapper.toResponse(savedAsset);
    }

    public List<AssetResponse> getMyAssets() {

        User user = currentUserService.getCurrentUser();

        return assetRepository.findAllByUser(user)
                .stream()
                .map(assetMapper::toResponse)
                .toList();
    }

    public AssetResponse getMyAsset(Long id) {

        User user = currentUserService.getCurrentUser();

        Asset asset = assetRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Asset not found")
                );

        return assetMapper.toResponse(asset);
    }
}