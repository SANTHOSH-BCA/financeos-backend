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
import org.slf4j.LoggerFactory;import java.math.BigDecimal;
import java.math.RoundingMode;import java.math.BigDecimal;
import java.math.RoundingMode;import com.financeos.financeosbackend.investment.dto.InvestmentPerformanceResponse;import com.financeos.financeosbackend.investment.dto.InvestmentHoldingPerformanceResponse;
import java.util.ArrayList;
import java.math.RoundingMode;import com.financeos.financeosbackend.investment.dto.InvestmentAllocationResponse;
import java.util.ArrayList;import com.financeos.financeosbackend.investment.dto.InvestmentValuationHistoryResponse;
import com.financeos.financeosbackend.investment.entity.InvestmentValuationHistory;
import com.financeos.financeosbackend.investment.repository.InvestmentValuationHistoryRepository;import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;import com.financeos.financeosbackend.investment.dto.InvestmentExposureResponse;import java.util.HashMap;
import java.util.Map;
import java.math.RoundingMode;import com.financeos.financeosbackend.networth.service.NetWorthService;


@Service
public class InvestmentService {

    private static final Logger logger =
            LoggerFactory.getLogger(InvestmentService.class);

    private final InvestmentRepository investmentRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final InvestmentValuationHistoryRepository valuationHistoryRepository;
    private final NetWorthService netWorthService;

    public InvestmentService(InvestmentRepository investmentRepository,
                             UserRepository userRepository,
                             CurrentUserService currentUserService,
                             InvestmentValuationHistoryRepository valuationHistoryRepository,
                             NetWorthService netWorthService) {

        this.investmentRepository = investmentRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.valuationHistoryRepository = valuationHistoryRepository;
        this.netWorthService = netWorthService;
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
        investment.setTotalInvestedAmount(request.getAmount());

        investment.setCurrentValue(
                request.getCurrentValue() != null
                        ? request.getCurrentValue()
                        : request.getAmount()
        );

        investment.setValuationDate(request.getInvestmentDate());
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

    public InvestmentPerformanceResponse getPortfolioPerformance() {

        User user = currentUserService.getCurrentUser();

        BigDecimal totalInvestedAmount =
                investmentRepository.getTotalInvestmentByUser(user);

        BigDecimal currentPortfolioValue =
                investmentRepository.getTotalCurrentValueByUser(user);

        BigDecimal totalProfitLoss =
                investmentRepository.getTotalProfitLossByUser(user);

        Long investmentCount =
                investmentRepository.countInvestmentsByUser(user);

        BigDecimal returnPercentage = BigDecimal.ZERO;

        if (totalInvestedAmount.compareTo(BigDecimal.ZERO) > 0) {
            returnPercentage = totalProfitLoss
                    .divide(totalInvestedAmount, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return new InvestmentPerformanceResponse(
                totalInvestedAmount,
                currentPortfolioValue,
                totalProfitLoss,
                returnPercentage,
                investmentCount
        );
    }

    public List<InvestmentHoldingPerformanceResponse> getHoldingPerformance() {

        User user = currentUserService.getCurrentUser();

        List<Investment> investments =
                investmentRepository.findByUser(user, Pageable.unpaged())
                        .getContent();

        List<InvestmentHoldingPerformanceResponse> responses =
                new ArrayList<>();

        for (Investment investment : investments) {

            BigDecimal investedAmount =
                    investment.getTotalInvestedAmount() != null
                            ? investment.getTotalInvestedAmount()
                            : investment.getAmount();

            BigDecimal currentValue =
                    investment.getCurrentValue() != null
                            ? investment.getCurrentValue()
                            : investedAmount;

            BigDecimal profitLoss =
                    currentValue.subtract(investedAmount);

            BigDecimal returnPercentage = BigDecimal.ZERO;

            if (investedAmount.compareTo(BigDecimal.ZERO) > 0) {
                returnPercentage = profitLoss
                        .divide(investedAmount, 6, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            responses.add(
                    new InvestmentHoldingPerformanceResponse(
                            investment.getId(),
                            investment.getInvestmentName(),
                            investment.getInvestmentType(),
                            investedAmount,
                            currentValue,
                            profitLoss,
                            returnPercentage
                    )
            );
        }

        return responses;
    }

    public List<InvestmentAllocationResponse> getAssetAllocation() {

        User user = currentUserService.getCurrentUser();

        List<Object[]> distribution =
                investmentRepository.getInvestmentDistributionByUser(user);

        BigDecimal totalInvested =
                investmentRepository.getTotalInvestmentByUser(user);

        List<InvestmentAllocationResponse> response = new ArrayList<>();

        for (Object[] row : distribution) {

            String investmentType = (String) row[0];
            BigDecimal investedAmount = (BigDecimal) row[1];

            BigDecimal allocationPercentage = BigDecimal.ZERO;

            if (totalInvested.compareTo(BigDecimal.ZERO) > 0) {
                allocationPercentage = investedAmount
                        .divide(totalInvested, 6, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            response.add(
                    new InvestmentAllocationResponse(
                            investmentType,
                            investedAmount,
                            allocationPercentage
                    )
            );
        }

        return response;
    }

    public List<InvestmentValuationHistoryResponse> getInvestmentHistory(Long investmentId) {

        User user = currentUserService.getCurrentUser();

        Investment investment = investmentRepository.findByIdAndUser(investmentId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Investment not found"));

        return valuationHistoryRepository
                .findByInvestmentOrderByValuationDateDesc(investment)
                .stream()
                .map(history -> new InvestmentValuationHistoryResponse(
                        history.getId(),
                        investment.getId(),
                        history.getInvestedAmount(),
                        history.getCurrentValue(),
                        history.getCurrentValue()
                                .subtract(history.getInvestedAmount()),
                        history.getValuationDate()
                ))
                .toList();
    }

    public InvestmentResponse createInvestmentFromTransaction(
            FinancialTransaction transaction) {

        User user = currentUserService.getCurrentUser();

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        if (investmentRepository.findByTransaction(transaction).isPresent()) {
            throw new IllegalStateException(
                    "Transaction has already been converted to an investment");
        }

        if (transaction.getStatus() != TransactionStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Only confirmed transactions can become investments");
        }

        if (transaction.getType() != TransactionType.INVESTMENT) {
            throw new IllegalStateException(
                    "Only investment transactions can become investments");
        }

        Investment investment = new Investment();

        investment.setInvestmentName(
                transaction.getMerchantPayee() != null
                        ? transaction.getMerchantPayee()
                        : "Transaction Investment"
        );

        investment.setInvestmentType(
                transaction.getCategory() != null
                        ? transaction.getCategory()
                        : "Other"
        );

        investment.setAmount(transaction.getAmount());
        investment.setTotalInvestedAmount(transaction.getAmount());
        investment.setCurrentValue(transaction.getAmount());
        investment.setInvestmentDate(
                transaction.getTransactionDateTime().toLocalDate()
        );
        investment.setValuationDate(
                transaction.getTransactionDateTime().toLocalDate()
        );
        investment.setUser(user);
        investment.setTransaction(transaction);

        Investment savedInvestment =
                investmentRepository.save(investment);

        return mapToResponse(savedInvestment);
    }

    public List<InvestmentExposureResponse> getInvestmentExposure() {

        User user = currentUserService.getCurrentUser();

        List<Investment> investments =
                investmentRepository.findByUser(
                        user,
                        org.springframework.data.domain.Pageable.unpaged()
                ).getContent();

        Map<String, BigDecimal> exposureByType = new HashMap<>();

        for (Investment investment : investments) {

            String investmentType = investment.getInvestmentType();

            if (investmentType == null || investmentType.isBlank()) {
                continue;
            }

            BigDecimal currentValue =
                    investment.getCurrentValue() != null
                            ? investment.getCurrentValue()
                            : investment.getTotalInvestedAmount() != null
                              ? investment.getTotalInvestedAmount()
                              : investment.getAmount();

            exposureByType.merge(
                    investmentType,
                    currentValue,
                    BigDecimal::add
            );
        }

        BigDecimal totalCurrentValue =
                exposureByType.values()
                        .stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<InvestmentExposureResponse> response = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : exposureByType.entrySet()) {

            BigDecimal exposurePercentage = BigDecimal.ZERO;

            if (totalCurrentValue.compareTo(BigDecimal.ZERO) > 0) {
                exposurePercentage =
                        entry.getValue()
                                .multiply(BigDecimal.valueOf(100))
                                .divide(
                                        totalCurrentValue,
                                        2,
                                        RoundingMode.HALF_UP
                                );
            }

            response.add(
                    new InvestmentExposureResponse(
                            entry.getKey(),
                            entry.getValue(),
                            exposurePercentage
                    )
            );
        }

        return response;
    }

    public BigDecimal calculateInvestmentToNetWorthPercentage() {

        BigDecimal investmentValue =
                investmentRepository.getTotalCurrentValueByUser(
                        currentUserService.getCurrentUser()
                );

        BigDecimal netWorth =
                netWorthService.calculateNetWorth();

        if (netWorth.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return investmentValue
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        netWorth,
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private void calculatePerformance(Investment investment) {

        BigDecimal investedAmount = investment.getTotalInvestedAmount();

        if (investedAmount == null) {
            investedAmount = investment.getAmount();
            investment.setTotalInvestedAmount(investedAmount);
        }

        BigDecimal currentValue = investment.getCurrentValue();

        if (currentValue == null) {
            currentValue = investedAmount;
            investment.setCurrentValue(currentValue);
        }

        BigDecimal profitLoss =
                currentValue.subtract(investedAmount);

        investment.setCurrentValue(currentValue);

        if (investedAmount.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal returnPercentage =
                    profitLoss
                            .divide(
                                    investedAmount,
                                    6,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(BigDecimal.valueOf(100));

            investment.setAmount(investedAmount);
        }
    }

    private InvestmentResponse mapToResponse(Investment investment) {

        InvestmentResponse response = new InvestmentResponse();

        BigDecimal investedAmount = investment.getTotalInvestedAmount();

        if (investedAmount == null) {
            investedAmount = investment.getAmount();
        }

        BigDecimal currentValue = investment.getCurrentValue();

        if (currentValue == null) {
            currentValue = investedAmount;
        }

        BigDecimal profitLoss =
                currentValue.subtract(investedAmount);

        BigDecimal returnPercentage = BigDecimal.ZERO;

        if (investedAmount.compareTo(BigDecimal.ZERO) > 0) {
            returnPercentage = profitLoss
                    .divide(investedAmount, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        response.setId(investment.getId());
        response.setInvestmentName(investment.getInvestmentName());
        response.setInvestmentType(investment.getInvestmentType());
        response.setAmount(investment.getAmount());
        response.setCurrentValue(currentValue);
        response.setTotalInvestedAmount(investedAmount);
        response.setProfitLoss(profitLoss);
        response.setReturnPercentage(returnPercentage);
        response.setInvestmentDate(investment.getInvestmentDate());
        response.setValuationDate(investment.getValuationDate());

        response.setTransactionId(
                investment.getTransaction() != null
                        ? investment.getTransaction().getId()
                        : null
        );

        return response;
    }
}