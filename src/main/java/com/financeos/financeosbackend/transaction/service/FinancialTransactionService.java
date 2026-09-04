package com.financeos.financeosbackend.transaction.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.transaction.dto.FinancialTransactionRequest;
import com.financeos.financeosbackend.transaction.dto.FinancialTransactionResponse;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import java.util.List;
import com.financeos.financeosbackend.transaction.dto.TransactionFilterRequest;
import com.financeos.financeosbackend.transaction.specification.FinancialTransactionSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financeos.financeosbackend.transaction.dto.UpdateTransactionTypeRequest;
import com.financeos.financeosbackend.transaction.dto.EditTransactionRequest;
import com.financeos.financeosbackend.expense.service.ExpenseService;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.dto.UpdateTransactionCategoryRequest;import com.financeos.financeosbackend.transaction.dto.TransactionClassificationResponse;import com.financeos.financeosbackend.transaction.dto.HelpTransactionRequest;import com.financeos.financeosbackend.transaction.dto.ReconcileHelpRequest;import java.util.List;import com.financeos.financeosbackend.expense.service.ExpenseService;import java.math.BigDecimal;import java.time.LocalDateTime;import com.financeos.financeosbackend.transaction.service.TransactionClassificationService;import com.financeos.financeosbackend.income.entity.Income;import com.financeos.financeosbackend.income.service.IncomeService;import com.financeos.financeosbackend.transaction.dto.SmsTransactionParseResult;
import com.financeos.financeosbackend.transaction.enums.TransactionSource;import org.springframework.transaction.annotation.Transactional;import java.util.ArrayList;import com.financeos.financeosbackend.transaction.dto.TransactionClassificationCorrectionResponse;import com.financeos.financeosbackend.investment.service.InvestmentService;import com.financeos.financeosbackend.goal.service.GoalService;import com.financeos.financeosbackend.goal.service.GoalService;

@Service
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final TransactionClassificationService classificationService;
    private final TransactionClassificationEngine classificationEngine;
    private final TransactionDuplicateDetectionService duplicateDetectionService;
    private final HelpReturnDetectionService helpReturnDetectionService;
    private final SmsTransactionParserService smsTransactionParserService;
    private final InvestmentService investmentService;
    private final GoalService goalService;

    public FinancialTransactionService(
            FinancialTransactionRepository transactionRepository,
            CurrentUserService currentUserService,
            ExpenseService expenseService,
            IncomeService incomeService,
            TransactionClassificationService classificationService,
            TransactionClassificationEngine classificationEngine,
            HelpReturnDetectionService helpReturnDetectionService,
            SmsTransactionParserService smsTransactionParserService,
            TransactionDuplicateDetectionService duplicateDetectionService,
            InvestmentService investmentService,
            GoalService goalService) {

        this.transactionRepository = transactionRepository;
        this.currentUserService = currentUserService;
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.classificationService = classificationService;
        this.classificationEngine = classificationEngine;
        this.duplicateDetectionService = duplicateDetectionService;
        this.helpReturnDetectionService = helpReturnDetectionService;
        this.smsTransactionParserService = smsTransactionParserService;
        this.investmentService = investmentService;
        this.goalService = goalService;
    }

    public FinancialTransactionResponse createTransaction(
            FinancialTransactionRequest request) {

        User user = currentUserService.getCurrentUser();

        validateTransactionDate(request.getTransactionDateTime());

        if (request.getType() == null) {
            throw new IllegalArgumentException(
                    "Transaction type is required"
            );
        }

        FinancialTransaction transaction = new FinancialTransaction();

        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDateTime(
                request.getTransactionDateTime()
        );
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setSource(
                com.financeos.financeosbackend.transaction.enums.TransactionSource.MANUAL
        );
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setMerchantPayee(request.getMerchantPayee());
        transaction.setDescription(request.getDescription());
        transaction.setReference(request.getReference());
        transaction.setLocation(request.getLocation());
        transaction.setGoalId(request.getGoalId());

        FinancialTransaction savedTransaction =
                transactionRepository.save(transaction);

        boolean possibleDuplicate =
                duplicateDetectionService.isPossibleDuplicate(
                        savedTransaction
                );

        if (possibleDuplicate) {

            savedTransaction.setStatus(
                    TransactionStatus.DUPLICATE
            );

            savedTransaction =
                    transactionRepository.save(savedTransaction);

        } else {

            TransactionClassificationEngine.ClassificationResult result =
                    classificationEngine.classify(savedTransaction);

            classificationService.createSuggestion(
                    savedTransaction,
                    result.suggestedType(),
                    result.suggestedCategory(),
                    result.confidence()
            );
        }

        return mapToResponse(savedTransaction);
    }

    public List<FinancialTransactionResponse> getPendingTransactions() {

        User user = currentUserService.getCurrentUser();

        List<TransactionStatus> inboxStatuses = List.of(
                TransactionStatus.PENDING,
                TransactionStatus.DUPLICATE
        );

        return transactionRepository
                .findInboxTransactions(user, inboxStatuses)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FinancialTransactionResponse confirmTransaction(Long id) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING
                && transaction.getStatus() != TransactionStatus.EDITED) {

            throw new IllegalStateException(
                    "Only pending or edited transactions can be confirmed"
            );
        }

        transaction.setStatus(TransactionStatus.CONFIRMED);

        FinancialTransaction savedTransaction =
                transactionRepository.save(transaction);

        if (savedTransaction.getType() == TransactionType.EXPENSE) {

            expenseService.createExpenseFromTransaction(
                    savedTransaction
            );

        } else if (savedTransaction.getType() == TransactionType.INCOME) {

            incomeService.createIncomeFromTransaction(
                    savedTransaction
            );
        } else if (savedTransaction.getType() == TransactionType.INVESTMENT) {
            investmentService.createInvestmentFromTransaction(savedTransaction);
        } else if (savedTransaction.getType() == TransactionType.GOAL_CONTRIBUTION) {
            if (savedTransaction.getGoalId() == null) {
                throw new IllegalStateException(
                        "Goal ID is required for goal contribution transactions");
            }

            goalService.createContributionFromTransaction(
                    savedTransaction,
                    savedTransaction.getGoalId()
            );
        }

        return mapToResponse(savedTransaction);
    }

    public FinancialTransactionResponse rejectTransaction(Long id) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending transactions can be rejected"
            );
        }

        transaction.setStatus(TransactionStatus.REJECTED);

        FinancialTransaction savedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(savedTransaction);
    }

    public FinancialTransactionResponse editTransaction(
            Long id,
            EditTransactionRequest request) {

        User user = currentUserService.getCurrentUser();

        validateTransactionDate(request.getTransactionDateTime());

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending transactions can be edited"
            );
        }

        TransactionClassificationResponse previousSuggestion =
                classificationService.getSuggestion(transaction);

        transaction.setAmount(request.getAmount());

        transaction.setTransactionDateTime(
                request.getTransactionDateTime()
        );

        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());

        transaction.setMerchantPayee(
                request.getMerchantPayee()
        );

        transaction.setDescription(
                request.getDescription()
        );

        transaction.setReference(
                request.getReference()
        );

        transaction.setLocation(
                request.getLocation()
        );

        transaction.setStatus(TransactionStatus.EDITED);

        FinancialTransaction updatedTransaction =
                transactionRepository.save(transaction);

        if (previousSuggestion != null) {
            classificationService.recordCorrection(
                    updatedTransaction,
                    request.getType(),
                    request.getCategory()
            );
        }

        TransactionClassificationEngine.ClassificationResult result =
                classificationEngine.classify(updatedTransaction);

        classificationService.createSuggestion(
                updatedTransaction,
                result.suggestedType(),
                result.suggestedCategory(),
                result.confidence()
        );

        return mapToResponse(updatedTransaction);
    }

    public FinancialTransactionResponse updateCategory(
            Long id,
            UpdateTransactionCategoryRequest request) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING
                && transaction.getStatus() != TransactionStatus.EDITED) {

            throw new IllegalStateException(
                    "Only pending or edited transactions can be categorized"
            );
        }

        classificationService.recordCorrection(
                transaction,
                transaction.getType(),
                request.getCategory()
        );

        transaction.setCategory(request.getCategory());

        FinancialTransaction updatedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(updatedTransaction);
    }

    public FinancialTransactionResponse updateType(
            Long id,
            UpdateTransactionTypeRequest request) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING
                && transaction.getStatus() != TransactionStatus.EDITED) {

            throw new IllegalStateException(
                    "Only pending or edited transactions can change type"
            );
        }

        classificationService.recordCorrection(
                transaction,
                request.getType(),
                transaction.getCategory()
        );

        transaction.setType(request.getType());

        FinancialTransaction updatedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(updatedTransaction);
    }

    public Page<FinancialTransactionResponse> getTransactions(
            TransactionFilterRequest request,
            Pageable pageable) {

        User user = currentUserService.getCurrentUser();

        Specification<FinancialTransaction> specification =
                FinancialTransactionSpecification.belongsToUser(user)
                        .and(FinancialTransactionSpecification.hasSearch(
                                request.getSearch()))
                        .and(FinancialTransactionSpecification.hasType(
                                request.getType()))
                        .and(FinancialTransactionSpecification.hasCategory(
                                request.getCategory()))
                        .and(FinancialTransactionSpecification.hasMinAmount(
                                request.getMinAmount()))
                        .and(FinancialTransactionSpecification.hasMaxAmount(
                                request.getMaxAmount()))
                        .and(FinancialTransactionSpecification.hasStartDate(
                                request.getStartDate()))
                        .and(FinancialTransactionSpecification.hasEndDate(
                                request.getEndDate()))
                        .and(FinancialTransactionSpecification.hasSource(
                                request.getSource()))
                        .and(FinancialTransactionSpecification.hasStatus(
                                request.getStatus()));

        return transactionRepository
                .findAll(specification, pageable)
                .map(this::mapToResponse);
    }

    public FinancialTransactionResponse getTransaction(Long id) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"));

        return mapToResponse(transaction);
    }

    public TransactionClassificationResponse getClassification(
            Long id) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"));

        return classificationService.getSuggestion(transaction);
    }

    public FinancialTransactionResponse markAsHelp(
            Long id,
            HelpTransactionRequest request) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING
                && transaction.getStatus() != TransactionStatus.EDITED) {

            throw new IllegalStateException(
                    "Only pending or edited transactions can be marked as help"
            );
        }

        if (request.getExpectedReturnDate()
                .isBefore(transaction.getTransactionDateTime().toLocalDate())) {

            throw new IllegalArgumentException(
                    "Expected return date cannot be before transaction date"
            );
        }

        transaction.setType(TransactionType.HELP_GIVEN);
        transaction.setExpectedReturnDate(
                request.getExpectedReturnDate()
        );
        transaction.setReturnedAmount(BigDecimal.ZERO);

        FinancialTransaction updatedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(updatedTransaction);
    }

    public FinancialTransactionResponse reconcileHelp(
            Long helpTransactionId,
            ReconcileHelpRequest request) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction helpTransaction =
                transactionRepository.findByIdAndUser(
                        helpTransactionId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Help transaction not found"
                        ));

        if (helpTransaction.getType()
                != TransactionType.HELP_GIVEN) {

            throw new IllegalStateException(
                    "Transaction is not a help transaction"
            );
        }

        if (helpTransaction.getStatus()
                != TransactionStatus.PENDING
                && helpTransaction.getStatus()
                != TransactionStatus.HELP_OVERDUE) {

            throw new IllegalStateException(
                    "Help transaction cannot be reconciled"
            );
        }

        FinancialTransaction returnTransaction =
                transactionRepository.findByIdAndUser(
                        request.getReturnTransactionId(),
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Return transaction not found"
                        ));

        if (returnTransaction.getId().equals(helpTransaction.getId())) {
            throw new IllegalArgumentException(
                    "Help transaction cannot be reconciled with itself"
            );
        }

        if (returnTransaction.getType()
                == TransactionType.HELP_RETURNED) {

            throw new IllegalStateException(
                    "Return transaction has already been reconciled"
            );
        }

        if (returnTransaction.getType()
                != TransactionType.INCOME) {

            throw new IllegalStateException(
                    "Return transaction must be an income transaction"
            );
        }

        if (returnTransaction.getStatus()
                != TransactionStatus.CONFIRMED) {

            throw new IllegalStateException(
                    "Return transaction must be confirmed"
            );
        }

        BigDecimal outstandingAmount =
                helpTransaction.getAmount()
                        .subtract(helpTransaction.getReturnedAmount());

        if (returnTransaction.getAmount()
                .compareTo(outstandingAmount) > 0) {

            throw new IllegalArgumentException(
                    "Return amount exceeds outstanding help amount"
            );
        }

        BigDecimal newReturnedAmount =
                helpTransaction.getReturnedAmount()
                        .add(returnTransaction.getAmount());

        helpTransaction.setReturnedAmount(newReturnedAmount);
        returnTransaction.setType(TransactionType.HELP_RETURNED);

        if (helpTransaction.getHelpReturns() == null) {
            helpTransaction.setHelpReturns(new ArrayList<>());
        }

        helpTransaction.getHelpReturns().add(returnTransaction);

        if (newReturnedAmount.compareTo(helpTransaction.getAmount()) == 0) {
            helpTransaction.setStatus(TransactionStatus.RECONCILED);
        }

        transactionRepository.save(returnTransaction);

        FinancialTransaction reconciledTransaction =
                transactionRepository.save(helpTransaction);

        return mapToResponse(reconciledTransaction);
    }

    public List<FinancialTransactionResponse> getPossibleHelpReturns(
            Long helpTransactionId) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction helpTransaction =
                transactionRepository.findByIdAndUser(
                        helpTransactionId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Help transaction not found"
                        ));

        if (helpTransaction.getType()
                != TransactionType.HELP_GIVEN) {

            throw new IllegalStateException(
                    "Transaction is not a help transaction"
            );
        }

        return helpReturnDetectionService
                .findPossibleReturns(helpTransaction)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FinancialTransactionResponse convertHelpToExpense(Long id) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"
                                ));

        if (transaction.getType() != TransactionType.HELP_GIVEN) {
            throw new IllegalStateException(
                    "Only help transactions can be converted to expenses"
            );
        }

        if (transaction.getStatus() != TransactionStatus.HELP_OVERDUE) {
            throw new IllegalStateException(
                    "Only overdue help transactions can be converted to expenses"
            );
        }

        expenseService.createExpenseFromHelpTransaction(transaction);

        transaction.setType(TransactionType.EXPENSE);
        transaction.setStatus(TransactionStatus.RECONCILED);

        FinancialTransaction updatedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(updatedTransaction);
    }

    public FinancialTransactionResponse createTransactionFromSms(
            String message) {

        User user = currentUserService.getCurrentUser();

        SmsTransactionParseResult parsed =
                smsTransactionParserService.parse(message);

        FinancialTransaction transaction =
                new FinancialTransaction();

        transaction.setUser(user);
        transaction.setAmount(parsed.getAmount());
        transaction.setTransactionDateTime(
                parsed.getTransactionDateTime()
        );

        transaction.setType(
                parsed.isDebit()
                        ? TransactionType.EXPENSE
                        : TransactionType.INCOME
        );

        transaction.setSource(
                TransactionSource.SMS
        );

        transaction.setStatus(
                TransactionStatus.PENDING
        );

        transaction.setMerchantPayee(
                parsed.getMerchantPayee()
        );

        transaction.setReference(
                parsed.getReference()
        );

        FinancialTransaction savedTransaction =
                transactionRepository.save(transaction);

        boolean possibleDuplicate =
                duplicateDetectionService.isPossibleDuplicate(
                        savedTransaction
                );

        if (possibleDuplicate) {

            savedTransaction.setStatus(
                    TransactionStatus.DUPLICATE
            );

            savedTransaction =
                    transactionRepository.save(savedTransaction);

        } else {

            TransactionClassificationEngine.ClassificationResult result =
                    classificationEngine.classify(savedTransaction);

            classificationService.createSuggestion(
                    savedTransaction,
                    result.suggestedType(),
                    result.suggestedCategory(),
                    result.confidence()
            );
        }

        return mapToResponse(savedTransaction);
    }

    @Transactional
    public FinancialTransactionResponse confirmHelpReturn(
            Long helpTransactionId,
            Long returnTransactionId) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction helpTransaction =
                transactionRepository.findByIdAndUser(
                        helpTransactionId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Help transaction not found"
                        ));

        FinancialTransaction returnTransaction =
                transactionRepository.findByIdAndUser(
                        returnTransactionId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Return transaction not found"
                        ));

        if (helpTransaction.getType() != TransactionType.HELP_GIVEN) {
            throw new IllegalStateException(
                    "Selected transaction is not a Help transaction"
            );
        }

        if (returnTransaction.getType() != TransactionType.INCOME) {
            throw new IllegalStateException(
                    "Return transaction must be an income transaction"
            );
        }

        if (returnTransaction.getAmount() == null
                || helpTransaction.getAmount() == null) {
            throw new IllegalStateException(
                    "Help and return amounts are required"
            );
        }

        if (returnTransaction.getAmount()
                .compareTo(helpTransaction.getAmount()) != 0) {

            throw new IllegalStateException(
                    "Return amount does not exactly match the Help amount"
            );
        }

        if (helpTransaction.getStatus() == TransactionStatus.RECONCILED) {
            throw new IllegalStateException(
                    "Help transaction has already been reconciled"
            );
        }

        helpTransaction.setStatus(TransactionStatus.RECONCILED);

        transactionRepository.save(helpTransaction);

        returnTransaction.setStatus(TransactionStatus.RECONCILED);

        FinancialTransaction savedReturnTransaction =
                transactionRepository.save(returnTransaction);

        return mapToResponse(savedReturnTransaction);
    }

    public List<TransactionClassificationCorrectionResponse>
    getClassificationCorrections(Long transactionId) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction transaction =
                transactionRepository.findByIdAndUser(
                                transactionId,
                                user
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"
                                ));

        return classificationService.getCorrectionHistory(
                transaction
        );
    }

    private void validateTransactionDate(LocalDateTime transactionDateTime) {

        if (transactionDateTime == null) {
            throw new IllegalArgumentException(
                    "Transaction date and time is required"
            );
        }

        if (transactionDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Transaction date and time cannot be in the future"
            );
        }
    }

    private FinancialTransactionResponse mapToResponse(
            FinancialTransaction transaction) {

        FinancialTransactionResponse response =
                new FinancialTransactionResponse();

        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setTransactionDateTime(
                transaction.getTransactionDateTime()
        );
        response.setType(transaction.getType());
        response.setCategory(transaction.getCategory());
        response.setSource(transaction.getSource());
        response.setGoalId(transaction.getGoalId());
        response.setStatus(transaction.getStatus());
        if (transaction.getExpense() != null) {
            response.setLinkedExpenseId(
                    transaction.getExpense().getId()
            );
        }
        response.setMerchantPayee(transaction.getMerchantPayee());
        response.setDescription(transaction.getDescription());
        response.setReference(transaction.getReference());
        response.setLocation(transaction.getLocation());

        response.setReturnedAmount(
                transaction.getReturnedAmount()
        );

        response.setOutstandingAmount(
                transaction.getType() == TransactionType.HELP_GIVEN
                        ? transaction.getAmount()
                        .subtract(transaction.getReturnedAmount())
                        : BigDecimal.ZERO
        );

        return response;
    }
}