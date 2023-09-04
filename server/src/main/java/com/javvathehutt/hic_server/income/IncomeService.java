package com.javvathehutt.hic_server.income;

import com.javvathehutt.hic_server.exception.ReadDataException;
import com.javvathehutt.hic_server.income.dto.IncomeDto;
import com.javvathehutt.hic_server.isdayof_client.IsdayofClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IsdayofClient isdayofClient;

    public IncomeDto getIncome(Integer year, Integer monthNumber, Integer salary) {
        int[] days = parseResponseBody(getDataByDay(year, monthNumber));
        Map<DayType, Integer> typeDay = countTypeDay(days);
        double hourlyIncome = hourlyIncomeCalc(salary, typeDay);
        String monthName = Month.of(monthNumber).name();
        return IncomeDto.builder()
                .year(year)
                .month(monthName)
                .salary(salary)
                .hour_income(hourlyIncome)
                .workdays_month(typeDay.get(DayType.WORKDAY) + typeDay.get(DayType.SHORTENED))
                .build();
    }

    private String getDataByDay(Integer year, Integer month) {
        return isdayofClient.getFromIsdayof(year, month);
    }


    private double hourlyIncomeCalc(int salary, Map<DayType, Integer> dayTypeCount) {
        int workHours = dayTypeCount.get(DayType.WORKDAY) * 8 + dayTypeCount.get(DayType.SHORTENED) * 7;
        double hourlyIncome = (double) salary / workHours;
        BigDecimal roundingResult = new BigDecimal(hourlyIncome);
        return roundingResult.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private Map<DayType, Integer> countTypeDay(int[] days) {
        Map<DayType, Integer> dayTypeCount = new HashMap<>();
        int workdayCount = 0;
        int reducedWHCount = 0;
        int weekendCount = 0;
        for (int day : days) {
            switch (day) {
                case 0 -> workdayCount++;
                case 1 -> weekendCount++;
                case 2 -> reducedWHCount++;
            }
        }
        dayTypeCount.put(DayType.WORKDAY, workdayCount);
        dayTypeCount.put(DayType.SHORTENED, reducedWHCount);
        dayTypeCount.put(DayType.WEEKEND, weekendCount);

        return dayTypeCount;
    }

    private int[] parseResponseBody(String responseBody) {
        String[] daysFromBody = responseBody.split("%0A");
        int[] days = new int[daysFromBody.length];
        try {
            for (int i = 0; i < daysFromBody.length; i++) {
                days[i] = parseInt(daysFromBody[i]);
            }
        } catch (NumberFormatException e) {
            throw new ReadDataException("error reading data from an information resource");
        }
        return days;
    }
}
