package com.javvathehutt.hic_gateway.income;

import com.javvathehutt.hic_gateway.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeClient incomeClient;

    @GetMapping("/hic/income")
    @ResponseBody
    public ResponseEntity<Object> getIncome(@RequestParam(name = "year") Integer year,
                                            @RequestParam(name = "month") String month,
                                            @RequestParam(name = "salary") Integer salary) {
        salaryCheck(salary);
        int monthNumber = getMonthNumber(month);
        dateCheck(year, monthNumber);
        return incomeClient.getIncome(year, monthNumber, salary);
    }

    @GetMapping(value = "/hic/main")
    public ResponseEntity<String> getIndex() {
        return incomeClient.getIndex();
    }

    private int getMonthNumber(String month) {
        DateTimeFormatter parserIgnoreCase = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMMM")
                .toFormatter(Locale.ENGLISH);
        return Month.from(parserIgnoreCase.parse(month)).getValue();
    }

    private void salaryCheck(int salary) {
        if (salary <= 0) {
            throw new BadRequestException("Salary can't be negative or zero");
        }
    }

    private void dateCheck(Integer year, Integer month) {
        LocalDate requestDate = LocalDate.of(year, month, 1);
        LocalDate currentDate = LocalDate.now();
        if (requestDate.isBefore(currentDate.minusYears(10)) ||
                requestDate.isAfter(currentDate.plusYears(10))) {
            throw new BadRequestException("Invalid date," +
                    " the year should be in the range of 10 years from the current one");
        }
    }
}
