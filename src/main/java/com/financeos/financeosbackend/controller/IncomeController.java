package com.financeos.financeosbackend.controller;

import com.financeos.financeosbackend.dto.AddIncomeRequest;
import com.financeos.financeosbackend.dto.IncomeResponse;
import com.financeos.financeosbackend.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    public List<IncomeResponse> getMyIncome() {

        return incomeService.getMyIncome();

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