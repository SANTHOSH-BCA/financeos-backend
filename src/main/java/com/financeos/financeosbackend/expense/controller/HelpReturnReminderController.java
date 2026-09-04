package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.expense.dto.HelpReturnReminderResponse;
import com.financeos.financeosbackend.expense.service.HelpReturnReminderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses/help-reminders")
public class HelpReturnReminderController {

    private final HelpReturnReminderService helpReturnReminderService;

    public HelpReturnReminderController(
            HelpReturnReminderService helpReturnReminderService) {

        this.helpReturnReminderService = helpReturnReminderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HelpReturnReminderResponse>>>
    getHelpReturnReminders() {

        List<HelpReturnReminderResponse> response =
                helpReturnReminderService.getHelpReturnReminders();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Help return reminders fetched successfully",
                        response
                )
        );
    }
}