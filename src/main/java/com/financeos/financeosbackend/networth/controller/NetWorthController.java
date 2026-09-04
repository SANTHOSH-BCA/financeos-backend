package com.financeos.financeosbackend.networth.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.networth.dto.NetWorthResponse;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v2/net-worth")
public class NetWorthController {

    private final NetWorthService netWorthService;

    public NetWorthController(NetWorthService netWorthService) {
        this.netWorthService = netWorthService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<NetWorthResponse>> getNetWorth() {

        BigDecimal recognizedAssets =
                netWorthService.calculateIncludedAssets();

        BigDecimal recognizedLiabilities =
                netWorthService.calculateIncludedLiabilities();

        BigDecimal netWorth =
                netWorthService.calculateNetWorth();

        NetWorthResponse response =
                new NetWorthResponse(
                        recognizedAssets,
                        recognizedLiabilities,
                        netWorth
                );

        return ResponseBuilder.success(
                "Net worth retrieved successfully",
                response
        );
    }
}