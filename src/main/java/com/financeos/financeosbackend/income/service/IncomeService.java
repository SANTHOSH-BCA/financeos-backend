package com.financeos.financeosbackend.income.service;

import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.income.dto.AddIncomeRequest;
import com.financeos.financeosbackend.income.dto.IncomeResponse;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import java.time.LocalDate;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class IncomeService {

    private static final Logger logger =
            LoggerFactory.getLogger(IncomeService.class);

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public IncomeService(IncomeRepository incomeRepository,
                         UserRepository userRepository,
                         CurrentUserService currentUserService) {

        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public IncomeResponse addIncome(AddIncomeRequest request) {

        logger.info("Creating income for source: {}", request.getSource());

        if (request.getIncomeDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Income date cannot be in the future");
        }

        Income income = new Income();

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());

        User user = currentUserService.getCurrentUser();

        income.setUser(user);

        Income savedIncome = incomeRepository.save(income);

        logger.info("Income created successfully with ID: {}", savedIncome.getId());

        IncomeResponse response = new IncomeResponse();

        response.setId(savedIncome.getId());
        response.setSource(savedIncome.getSource());
        response.setAmount(savedIncome.getAmount());
        response.setIncomeDate(savedIncome.getIncomeDate());

        return response;

    }

    public Page<IncomeResponse> getMyIncome(Pageable pageable) {

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

    public IncomeResponse updateIncome(Long id, AddIncomeRequest request) {

        User user = currentUserService.getCurrentUser();

        logger.info("Updating income with ID: {} for user: {}", id, user.getEmail());

        Optional<Income> optionalIncome =
                incomeRepository.findByIdAndUser(id, user);

        if (optionalIncome.isEmpty()) {
            throw new ResourceNotFoundException("Income not found");
        }

        Income income = optionalIncome.get();

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());

        Income updatedIncome = incomeRepository.save(income);

        logger.info("Income updated successfully with ID: {}", updatedIncome.getId());

        IncomeResponse response = new IncomeResponse();

        response.setId(updatedIncome.getId());
        response.setSource(updatedIncome.getSource());
        response.setAmount(updatedIncome.getAmount());
        response.setIncomeDate(updatedIncome.getIncomeDate());

        return response;

    }

    public void deleteIncome(Long id) {

        User user = currentUserService.getCurrentUser();

        Optional<Income> optionalIncome =
                incomeRepository.findByIdAndUser(id, user);

        if (optionalIncome.isEmpty()) {
            throw new ResourceNotFoundException("Income not found");
        }

        incomeRepository.delete(optionalIncome.get());

    }

    private IncomeResponse mapToResponse(Income income) {

        IncomeResponse response = new IncomeResponse();

        response.setId(income.getId());
        response.setSource(income.getSource());
        response.setAmount(income.getAmount());
        response.setIncomeDate(income.getIncomeDate());

        return response;
    }

}