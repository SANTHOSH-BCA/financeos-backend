package com.financeos.financeosbackend.service;

import com.financeos.financeosbackend.repository.IncomeRepository;
import com.financeos.financeosbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.dto.AddIncomeRequest;
import com.financeos.financeosbackend.dto.IncomeResponse;
import com.financeos.financeosbackend.entity.Income;
import com.financeos.financeosbackend.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    public IncomeResponse addIncome(AddIncomeRequest request) {

        Income income = new Income();

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).get();

        income.setUser(user);

        Income savedIncome = incomeRepository.save(income);

        IncomeResponse response = new IncomeResponse();

        response.setId(savedIncome.getId());
        response.setSource(savedIncome.getSource());
        response.setAmount(savedIncome.getAmount());
        response.setIncomeDate(savedIncome.getIncomeDate());

        return response;

    }

    public List<IncomeResponse> getMyIncome() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).get();

        List<Income> incomes = incomeRepository.findByUser(user);

        List<IncomeResponse> responses = new ArrayList<>();

        for (Income income : incomes) {

            IncomeResponse response = new IncomeResponse();

            response.setId(income.getId());
            response.setSource(income.getSource());
            response.setAmount(income.getAmount());
            response.setIncomeDate(income.getIncomeDate());

            responses.add(response);

        }

        return responses;

    }

    public IncomeResponse updateIncome(Long id, AddIncomeRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).get();

        Optional<Income> optionalIncome =
                incomeRepository.findByIdAndUser(id, user);

        if (optionalIncome.isEmpty()) {
            throw new RuntimeException("Income not found");
        }

        Income income = optionalIncome.get();

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());

        Income updatedIncome = incomeRepository.save(income);

        IncomeResponse response = new IncomeResponse();

        response.setId(updatedIncome.getId());
        response.setSource(updatedIncome.getSource());
        response.setAmount(updatedIncome.getAmount());
        response.setIncomeDate(updatedIncome.getIncomeDate());

        return response;

    }

    public String deleteIncome(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).get();

        Optional<Income> optionalIncome =
                incomeRepository.findByIdAndUser(id, user);

        if (optionalIncome.isEmpty()) {
            throw new RuntimeException("Income not found");
        }

        incomeRepository.delete(optionalIncome.get());

        return "Income Deleted Successfully";

    }

}