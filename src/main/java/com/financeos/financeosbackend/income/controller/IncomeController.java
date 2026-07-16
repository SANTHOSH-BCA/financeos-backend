package com.financeos.financeosbackend.income.controller;

import com.financeos.financeosbackend.income.dto.AddIncomeRequest;
import com.financeos.financeosbackend.income.dto.IncomeResponse;
import com.financeos.financeosbackend.income.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public IncomeResponse addIncome(@Valid @RequestBody AddIncomeRequest request) {

        return incomeService.addIncome(request);

    }

    @GetMapping
    public Page<IncomeResponse> getMyIncome(Pageable pageable) {

        return incomeService.getMyIncome(pageable);

    }

    @PutMapping("/{id}")
    public IncomeResponse updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody AddIncomeRequest request) {

        return incomeService.updateIncome(id, request);

    }

    @DeleteMapping("/{id}")
    public String deleteIncome(@PathVariable Long id) {

        return incomeService.deleteIncome(id);

    }

}