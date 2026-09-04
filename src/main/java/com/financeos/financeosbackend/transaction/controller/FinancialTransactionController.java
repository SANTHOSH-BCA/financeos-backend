package com.financeos.financeosbackend.transaction.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.transaction.dto.FinancialTransactionRequest;
import com.financeos.financeosbackend.transaction.dto.FinancialTransactionResponse;
import com.financeos.financeosbackend.transaction.service.FinancialTransactionService;
import jakarta.validation.Valid;
import com.financeos.financeosbackend.transaction.dto.TransactionClassificationResponse;
import com.financeos.financeosbackend.common.dto.PagedResponse;
import com.financeos.financeosbackend.transaction.dto.UpdateTransactionCategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.financeos.financeosbackend.transaction.dto.TransactionFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financeos.financeosbackend.transaction.dto.UpdateTransactionTypeRequest;
import com.financeos.financeosbackend.transaction.dto.EditTransactionRequest;import com.financeos.financeosbackend.transaction.dto.HelpTransactionRequest;import com.financeos.financeosbackend.transaction.dto.ReconcileHelpRequest;import org.springdoc.core.annotations.ParameterObject;import com.financeos.financeosbackend.transaction.dto.SmsTransactionRequest;
import jakarta.validation.Valid;import org.springframework.http.HttpStatus;import com.financeos.financeosbackend.expense.dto.HelpReturnConfirmationRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;import com.financeos.financeosbackend.transaction.dto.TransactionClassificationCorrectionResponse;
@RestController
@RequestMapping("/api/v1/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    public FinancialTransactionController(
            FinancialTransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> createTransaction(
            @Valid @RequestBody FinancialTransactionRequest request) {

        FinancialTransactionResponse response =
                transactionService.createTransaction(request);

        return ResponseBuilder.created(
                "Transaction created successfully",
                response
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<FinancialTransactionResponse>>> getPendingTransactions() {

        List<FinancialTransactionResponse> response =
                transactionService.getPendingTransactions();

        return ResponseBuilder.success(
                "Pending transactions fetched successfully",
                response
        );
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> confirmTransaction(
            @PathVariable Long id) {

        FinancialTransactionResponse response =
                transactionService.confirmTransaction(id);

        return ResponseBuilder.success(
                "Transaction confirmed successfully",
                response
        );
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> rejectTransaction(
            @PathVariable Long id) {

        FinancialTransactionResponse response =
                transactionService.rejectTransaction(id);

        return ResponseBuilder.success(
                "Transaction rejected successfully",
                response
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> editTransaction(
            @PathVariable Long id,
            @Valid @RequestBody EditTransactionRequest request) {

        FinancialTransactionResponse response =
                transactionService.editTransaction(id, request);

        return ResponseBuilder.success(
                "Transaction edited successfully",
                response
        );
    }

    @PutMapping("/{id}/category")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransactionCategoryRequest request) {

        FinancialTransactionResponse response =
                transactionService.updateCategory(id, request);

        return ResponseBuilder.success(
                "Transaction category updated successfully",
                response
        );
    }

    @PutMapping("/{id}/type")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> updateType(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransactionTypeRequest request) {

        FinancialTransactionResponse response =
                transactionService.updateType(id, request);

        return ResponseBuilder.success(
                "Transaction type updated successfully",
                response
        );
    }

    @GetMapping
    public ResponseEntity<PagedResponse<FinancialTransactionResponse>> getTransactions(
            TransactionFilterRequest request,
            @ParameterObject Pageable pageable) {

        Page<FinancialTransactionResponse> response =
                transactionService.getTransactions(request, pageable);

        return ResponseBuilder.paged(
                "Transactions fetched successfully",
                response
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> getTransaction(
            @PathVariable Long id) {

        FinancialTransactionResponse response =
                transactionService.getTransaction(id);

        return ResponseBuilder.success(
                "Transaction fetched successfully",
                response
        );
    }

    @GetMapping("/{id}/classification")
    public ResponseEntity<ApiResponse<TransactionClassificationResponse>> getClassification(
            @PathVariable Long id) {

        TransactionClassificationResponse response =
                transactionService.getClassification(id);

        return ResponseBuilder.success(
                "Transaction classification fetched successfully",
                response
        );
    }

    @GetMapping("/{id}/classification/corrections")
    public ResponseEntity<ApiResponse<List<TransactionClassificationCorrectionResponse>>>
    getClassificationCorrections(@PathVariable Long id) {

        List<TransactionClassificationCorrectionResponse> response =
                transactionService.getClassificationCorrections(id);

        return ResponseBuilder.success(
                "Transaction classification corrections fetched successfully",
                response
        );
    }

    @PostMapping("/{id}/help")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> markAsHelp(
            @PathVariable Long id,
            @Valid @RequestBody HelpTransactionRequest request) {

        FinancialTransactionResponse response =
                transactionService.markAsHelp(id, request);

        return ResponseBuilder.success(
                "Transaction marked as help successfully",
                response
        );
    }

    @PostMapping("/{id}/reconcile")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> reconcileHelp(
            @PathVariable Long id,
            @Valid @RequestBody ReconcileHelpRequest request) {

        FinancialTransactionResponse response =
                transactionService.reconcileHelp(id, request);

        return ResponseBuilder.success(
                "Help transaction reconciled successfully",
                response
        );
    }

    @GetMapping("/{id}/possible-returns")
    public ResponseEntity<ApiResponse<List<FinancialTransactionResponse>>> getPossibleHelpReturns(
            @PathVariable Long id) {

        List<FinancialTransactionResponse> response =
                transactionService.getPossibleHelpReturns(id);

        return ResponseBuilder.success(
                "Possible help returns fetched successfully",
                response
        );
    }

    @PostMapping("/{id}/convert-to-expense")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>> convertHelpToExpense(
            @PathVariable Long id) {

        FinancialTransactionResponse response =
                transactionService.convertHelpToExpense(id);

        return ResponseBuilder.success(
                "Help transaction converted to expense successfully",
                response
        );
    }

    @PostMapping("/sms")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>>
    createTransactionFromSms(
            @Valid @RequestBody SmsTransactionRequest request) {

        FinancialTransactionResponse response =
                transactionService.createTransactionFromSms(
                        request.getMessage()
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                HttpStatus.CREATED.value(),
                                "SMS transaction detected successfully",
                                response
                        )
                );
    }

    @PostMapping("/{helpTransactionId}/help-return/confirm")
    public ResponseEntity<ApiResponse<FinancialTransactionResponse>>
    confirmHelpReturn(
            @PathVariable Long helpTransactionId,
            @Valid @RequestBody HelpReturnConfirmationRequest request) {

        FinancialTransactionResponse response =
                transactionService.confirmHelpReturn(
                        helpTransactionId,
                        request.getReturnTransactionId()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Help return confirmed successfully",
                        response
                )
        );
    }
}