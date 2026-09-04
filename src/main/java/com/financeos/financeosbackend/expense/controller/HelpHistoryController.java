package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.expense.dto.HelpHistoryResponse;
import com.financeos.financeosbackend.expense.service.HelpHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expenses/help-history")
public class HelpHistoryController {

    private final HelpHistoryService helpHistoryService;

    public HelpHistoryController(
            HelpHistoryService helpHistoryService) {
        this.helpHistoryService = helpHistoryService;
    }

    @GetMapping("/{helpTransactionId}")
    public ResponseEntity<ApiResponse<HelpHistoryResponse>>
    getHelpHistory(
            @PathVariable Long helpTransactionId) {

        HelpHistoryResponse response =
                helpHistoryService.getHelpHistory(
                        helpTransactionId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Help history fetched successfully",
                        response
                )
        );
    }
}