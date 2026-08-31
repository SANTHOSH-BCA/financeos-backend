package com.financeos.financeosbackend.asset.controller;

import com.financeos.financeosbackend.asset.dto.AssetResponse;
import com.financeos.financeosbackend.asset.dto.CreateAssetRequest;
import com.financeos.financeosbackend.asset.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(
            @Valid @RequestBody CreateAssetRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assetService.createAsset(request));
    }

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getMyAssets() {

        return ResponseEntity.ok(
                assetService.getMyAssets()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getMyAsset(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                assetService.getMyAsset(id)
        );
    }
}