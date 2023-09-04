package com.javvathehutt.hic_gateway.income.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor

@Setter
@Getter

@Builder
public class IncomeDto {
    private Integer year;
    private String month;
    private Integer salary;
    private Double hour_income;
    private Integer workdays_month;
}
