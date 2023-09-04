package com.javvathehutt.hic_server.income;

import com.javvathehutt.hic_server.income.dto.IncomeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hic")
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;

    @GetMapping("/income")
    public IncomeDto getIncome(@RequestParam(name = "year", required = false) Integer year,
                               @RequestParam(name = "month", required = false) Integer month,
                               @RequestParam(name = "salary", required = false) Integer salary) {

        return incomeService.getIncome(year, month, salary);
    }
}
