package com.financeos.financeosbackend.income.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.income.dto.AddIncomeRequest;
import com.financeos.financeosbackend.income.dto.IncomeResponse;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;import com.financeos.financeosbackend.income.enums.IncomeSource;import com.financeos.financeosbackend.income.enums.IncomePattern;import com.financeos.financeosbackend.income.dto.MonthlyIncomeResponse;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IncomeService {

    private static final Logger logger =
            LoggerFactory.getLogger(IncomeService.class);

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public IncomeService(
            IncomeRepository incomeRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService) {

        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public IncomeResponse addIncome(AddIncomeRequest request) {

        logger.info(
                "Creating income for source: {}",
                request.getSource()
        );

        validateIncomeDate(request.getIncomeDate());

        validateIncomeSource(request.getSource());

        Income income = new Income();

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());
        income.setPattern(request.getPattern());

        User user = currentUserService.getCurrentUser();

        income.setUser(user);

        Income savedIncome =
                incomeRepository.save(income);

        logger.info(
                "Income created successfully with ID: {}",
                savedIncome.getId()
        );

        return mapToResponse(savedIncome);
    }

    public Page<IncomeResponse> getMyIncome(
            Pageable pageable) {

        User user = currentUserService.getCurrentUser();

        logger.info(
                "Fetching incomes for user: {} | Page: {} | Size: {}",
                user.getEmail(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        return incomeRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public IncomeResponse updateIncome(
            Long id,
            AddIncomeRequest request) {

        User user = currentUserService.getCurrentUser();

        logger.info(
                "Updating income with ID: {} for user: {}",
                id,
                user.getEmail()
        );

        validateIncomeDate(request.getIncomeDate());

        validateIncomeSource(request.getSource());

        Optional<Income> optionalIncome =
                incomeRepository.findByIdAndUser(id, user);

        if (optionalIncome.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Income not found"
            );
        }

        Income income = optionalIncome.get();

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());
        income.setPattern(request.getPattern());

        Income updatedIncome =
                incomeRepository.save(income);

        logger.info(
                "Income updated successfully with ID: {}",
                updatedIncome.getId()
        );

        return mapToResponse(updatedIncome);
    }

    public void deleteIncome(Long id) {

        User user = currentUserService.getCurrentUser();

        Optional<Income> optionalIncome =
                incomeRepository.findByIdAndUser(id, user);

        if (optionalIncome.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Income not found"
            );
        }

        incomeRepository.delete(optionalIncome.get());
    }

    public IncomeResponse createIncomeFromTransaction(
            FinancialTransaction transaction) {

        User user = currentUserService.getCurrentUser();

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException(
                    "Transaction not found"
            );
        }

        if (incomeRepository.findByTransaction(transaction).isPresent()) {
            throw new IllegalStateException(
                    "Transaction has already been converted to income"
            );
        }

        if (transaction.getStatus()
                != TransactionStatus.CONFIRMED) {

            throw new IllegalStateException(
                    "Only confirmed transactions can become income"
            );
        }

        if (transaction.getType()
                != TransactionType.INCOME) {

            throw new IllegalStateException(
                    "Only income transactions can become income"
            );
        }

        Income income = new Income();

        income.setSource(
                transaction.getMerchantPayee() != null
                        ? transaction.getMerchantPayee()
                        : "Transaction Income"
        );

        income.setAmount(
                transaction.getAmount()
        );

        income.setIncomeDate(
                transaction.getTransactionDateTime()
                        .toLocalDate()
        );

        income.setUser(user);
        income.setTransaction(transaction);
        income.setPattern(IncomePattern.IRREGULAR);

        Income savedIncome =
                incomeRepository.save(income);

        logger.info(
                "Income created from transaction with ID: {}",
                transaction.getId()
        );

        return mapToResponse(savedIncome);
    }

    private void validateIncomeSource(String source) {

        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException(
                    "Income source is required"
            );
        }

        try {
            String normalizedSource = source.trim()
                    .toUpperCase()
                    .replace(" ", "_");

            if (normalizedSource.equals("FREELANCING")) {
                normalizedSource = "FREELANCE";
            }

            IncomeSource.valueOf(normalizedSource);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid income source. Supported sources: "
                            + "Salary, Business, Freelance, Pocket Money, "
                            + "Family Support, Gift, Interest, Rental, "
                            + "Pension, Investment Income, Other"
            );
        }
    }

    public List<MonthlyIncomeResponse> getMonthlyIncomeHistory() {

        User user = currentUserService.getCurrentUser();

        return incomeRepository.findByUser(user)
                .stream()
                .collect(Collectors.groupingBy(
                        income -> YearMonth.from(
                                income.getIncomeDate()
                        ),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Income::getAmount,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        new MonthlyIncomeResponse(
                                entry.getKey().toString(),
                                entry.getValue()
                        )
                )
                .toList();
    }

    private void validateIncomeDate(
            LocalDate incomeDate) {

        if (incomeDate == null) {
            throw new IllegalArgumentException(
                    "Income date is required"
            );
        }

        if (incomeDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Income date cannot be in the future"
            );
        }
    }

    private IncomeResponse mapToResponse(
            Income income) {

        IncomeResponse response =
                new IncomeResponse();

        response.setId(income.getId());
        response.setSource(income.getSource());
        response.setAmount(income.getAmount());
        response.setIncomeDate(income.getIncomeDate());
        response.setPattern(income.getPattern());

        response.setTransactionId(
                income.getTransaction() != null
                        ? income.getTransaction().getId()
                        : null
        );

        return response;
    }
}