package com.financeos.financeosbackend.income.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.income.dto.IncomeForecastResponse;
import com.financeos.financeosbackend.income.service.IncomeForecastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incomes/forecast")
public class IncomeForecastController {

    private final IncomeForecastService incomeForecastService;

    public IncomeForecastController(
            IncomeForecastService incomeForecastService) {

        this.incomeForecastService =
                incomeForecastService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<IncomeForecastResponse>>
    getForecast() {

        IncomeForecastResponse response =
                incomeForecastService.getForecast();

        return ResponseBuilder.success(
                "Income forecast fetched successfully",
                response
        );
    }
}