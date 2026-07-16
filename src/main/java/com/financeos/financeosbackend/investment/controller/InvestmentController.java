package com.financeos.financeosbackend.investment.controller;

import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;
import com.financeos.financeosbackend.investment.dto.InvestmentResponse;
import com.financeos.financeosbackend.investment.service.InvestmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @GetMapping("/test")
    public String test() {
        return "Investment Controller Working";
    }

    @PostMapping
    public InvestmentResponse addInvestment(@Valid @RequestBody AddInvestmentRequest request) {
        return investmentService.addInvestment(request);
    }

    @GetMapping
    public Page<InvestmentResponse> getMyInvestments(Pageable pageable) {

        return investmentService.getMyInvestments(pageable);

    }

    @PutMapping("/{id}")
    public InvestmentResponse updateInvestment(@PathVariable Long id,
                                               @Valid @RequestBody AddInvestmentRequest request) {

        return investmentService.updateInvestment(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteInvestment(@PathVariable Long id) {

        investmentService.deleteInvestment(id);

    }

}