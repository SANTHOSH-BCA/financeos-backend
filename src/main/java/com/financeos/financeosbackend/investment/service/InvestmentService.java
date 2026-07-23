package com.financeos.financeosbackend.investment.service;

import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;
import com.financeos.financeosbackend.investment.dto.InvestmentResponse;
import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class InvestmentService {

    private static final Logger logger =
            LoggerFactory.getLogger(InvestmentService.class);

    private final InvestmentRepository investmentRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public InvestmentService(InvestmentRepository investmentRepository,
                             UserRepository userRepository,
                             CurrentUserService currentUserService) {

        this.investmentRepository = investmentRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public InvestmentResponse addInvestment(AddInvestmentRequest request) {

        if (request.getInvestmentDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Investment date cannot be in the future");
        }

        User user = currentUserService.getCurrentUser();

        logger.info("Creating investment '{}' for user: {}", request.getInvestmentName(), user.getEmail());

        Investment investment = new Investment();
        investment.setInvestmentName(request.getInvestmentName());
        investment.setInvestmentType(request.getInvestmentType());
        investment.setAmount(request.getAmount());
        investment.setInvestmentDate(request.getInvestmentDate());
        investment.setUser(user);

        Investment savedInvestment = investmentRepository.save(investment);

        logger.info("Investment created successfully with ID: {}", savedInvestment.getId());

        return mapToResponse(savedInvestment);
    }

    public Page<InvestmentResponse> getMyInvestments(Pageable pageable) {

        User user = currentUserService.getCurrentUser();

        return investmentRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public InvestmentResponse updateInvestment(Long id, AddInvestmentRequest request) {

        User user = currentUserService.getCurrentUser();

        logger.info("Updating investment with ID: {} for user: {}", id, user.getEmail());

        Investment investment = investmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));

        investment.setInvestmentName(request.getInvestmentName());
        investment.setInvestmentType(request.getInvestmentType());
        investment.setAmount(request.getAmount());
        investment.setInvestmentDate(request.getInvestmentDate());

        Investment updatedInvestment = investmentRepository.save(investment);

        logger.info("Investment updated successfully with ID: {}", updatedInvestment.getId());

        return mapToResponse(updatedInvestment);
    }

    public void deleteInvestment(Long id) {

        User user = currentUserService.getCurrentUser();

        logger.info("Deleting investment with ID: {} for user: {}", id, user.getEmail());

        Investment investment = investmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));

        investmentRepository.delete(investment);

        logger.info("Investment deleted successfully with ID: {}", id);
    }

    private InvestmentResponse mapToResponse(Investment investment) {

        InvestmentResponse response = new InvestmentResponse();

        response.setId(investment.getId());
        response.setInvestmentName(investment.getInvestmentName());
        response.setInvestmentType(investment.getInvestmentType());
        response.setAmount(investment.getAmount());
        response.setInvestmentDate(investment.getInvestmentDate());

        return response;
    }
}