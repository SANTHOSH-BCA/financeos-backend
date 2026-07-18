package com.financeos.financeosbackend.investment.service;

import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;
import com.financeos.financeosbackend.investment.dto.InvestmentResponse;
import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final UserRepository userRepository;

    public InvestmentService(InvestmentRepository investmentRepository,
                             UserRepository userRepository) {
        this.investmentRepository = investmentRepository;
        this.userRepository = userRepository;
    }

    public InvestmentResponse addInvestment(AddInvestmentRequest request) {

        if (request.getInvestmentDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Investment date cannot be in the future");
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Investment investment = new Investment();
        investment.setInvestmentName(request.getInvestmentName());
        investment.setInvestmentType(request.getInvestmentType());
        investment.setAmount(request.getAmount());
        investment.setInvestmentDate(request.getInvestmentDate());
        investment.setUser(user);

        Investment savedInvestment = investmentRepository.save(investment);

        return mapToResponse(savedInvestment);
    }

    public Page<InvestmentResponse> getMyInvestments(Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return investmentRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public InvestmentResponse updateInvestment(Long id, AddInvestmentRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Investment investment = investmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));

        investment.setInvestmentName(request.getInvestmentName());
        investment.setInvestmentType(request.getInvestmentType());
        investment.setAmount(request.getAmount());
        investment.setInvestmentDate(request.getInvestmentDate());

        Investment updatedInvestment = investmentRepository.save(investment);

        return mapToResponse(updatedInvestment);
    }

    public void deleteInvestment(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Investment investment = investmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found"));

        investmentRepository.delete(investment);
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