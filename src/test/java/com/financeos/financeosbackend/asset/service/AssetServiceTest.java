package com.financeos.financeosbackend.asset.service;

import com.financeos.financeosbackend.asset.dto.AssetResponse;
import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.entity.Asset;
import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import com.financeos.financeosbackend.asset.mapper.AssetMapper;
import com.financeos.financeosbackend.asset.repository.AssetRepository;
import com.financeos.financeosbackend.asset.validator.AssetValidator;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private AssetValidator assetValidator;

    @InjectMocks
    private AssetService assetService;

    @Test
    void createAsset_ShouldCreateSuccessfully() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        CreateAssetRequest request = new CreateAssetRequest();
        request.setAssetName("Gold");
        request.setAssetType(AssetType.GOLD);
        request.setTotalValue(new BigDecimal("200000"));
        request.setOwnershipType(OwnershipType.INDIVIDUAL);
        request.setOwnershipPercentage(new BigDecimal("100"));
        request.setValuationDate(LocalDate.now());

        Asset asset = new Asset();
        Asset savedAsset = new Asset();

        AssetResponse response = new AssetResponse();
        response.setId(1L);
        response.setAssetName("Gold");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(assetMapper.toEntity(request))
                .thenReturn(asset);

        when(assetRepository.save(asset))
                .thenReturn(savedAsset);

        when(assetMapper.toResponse(savedAsset))
                .thenReturn(response);

        AssetResponse result = assetService.createAsset(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gold", result.getAssetName());

        verify(assetValidator).validateOwnership(request);
        verify(currentUserService).getCurrentUser();
        verify(assetMapper).toEntity(request);
        verify(assetRepository).save(asset);
        verify(assetMapper).toResponse(savedAsset);
    }

    @Test
    void getMyAssets_ShouldReturnCurrentUsersAssets() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Asset asset = new Asset();
        AssetResponse response = new AssetResponse();
        response.setId(1L);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(assetRepository.findAllByUser(user))
                .thenReturn(java.util.List.of(asset));

        when(assetMapper.toResponse(asset))
                .thenReturn(response);

        var result = assetService.getMyAssets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(currentUserService).getCurrentUser();
        verify(assetRepository).findAllByUser(user);
        verify(assetMapper).toResponse(asset);
    }

    @Test
    void getMyAsset_ShouldReturnAssetBelongingToCurrentUser() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Asset asset = new Asset();
        AssetResponse response = new AssetResponse();
        response.setId(1L);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(assetRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(asset));

        when(assetMapper.toResponse(asset))
                .thenReturn(response);

        AssetResponse result = assetService.getMyAsset(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(currentUserService).getCurrentUser();
        verify(assetRepository).findByIdAndUser(1L, user);
        verify(assetMapper).toResponse(asset);
    }

    @Test
    void getMyAsset_ShouldThrowException_WhenAssetDoesNotExist() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(assetRepository.findByIdAndUser(999L, user))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> assetService.getMyAsset(999L)
                );

        assertEquals("Asset not found", exception.getMessage());

        verify(assetRepository).findByIdAndUser(999L, user);
        verify(assetMapper, never()).toResponse(any());
    }
}