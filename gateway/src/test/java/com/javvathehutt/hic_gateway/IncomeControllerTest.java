package com.javvathehutt.hic_gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javvathehutt.hic_gateway.exception.ErrorHandler;
import com.javvathehutt.hic_gateway.income.IncomeClient;
import com.javvathehutt.hic_gateway.income.IncomeController;
import com.javvathehutt.hic_gateway.income.dto.IncomeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class IncomeControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private MockMvc mockMvc;

    @Mock
    private IncomeClient incomeClient;

    @InjectMocks
    private IncomeController incomeController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(incomeController)
                .setControllerAdvice(ErrorHandler.class)
                .build();
    }

    @Test
    void shouldReturnIncomeDto() throws Exception {
        IncomeDto incomeDto1 = IncomeDto.builder()
                .year(2023)
                .month("JANUARY")
                .salary(1_500_000)
                .hour_income(11029.41)
                .workdays_month(17)
                .build();

        ResponseEntity<Object> response = new ResponseEntity<>(incomeDto1, HttpStatus.OK);
        when(incomeClient.getIncome(2023, 1, 1_500_000)).thenReturn(response);

        mockMvc.perform(get("/hic/income")
                        .queryParam("year", incomeDto1.getYear().toString())
                        .queryParam("month", incomeDto1.getMonth())
                        .queryParam("salary", incomeDto1.getSalary().toString()))

                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(incomeDto1)));
    }

    @Test
    void shouldReturnBadRequestMonth() throws Exception {
        IncomeDto incomeDto1 = IncomeDto.builder()
                .year(2023)
                .month("JARY")
                .salary(1_500_000)
                .hour_income(11029.41)
                .workdays_month(17)
                .build();

        mockMvc.perform(get("/hic/income")
                        .queryParam("year", incomeDto1.getYear().toString())
                        .queryParam("month", incomeDto1.getMonth())
                        .queryParam("salary", incomeDto1.getSalary().toString()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestYear() throws Exception {
        IncomeDto incomeDto1 = IncomeDto.builder()
                .year(2063)
                .month("JANUARY")
                .salary(1_500_000)
                .hour_income(11029.41)
                .workdays_month(17)
                .build();

        mockMvc.perform(get("/hic/income")
                        .queryParam("year", incomeDto1.getYear().toString())
                        .queryParam("month", incomeDto1.getMonth())
                        .queryParam("salary", incomeDto1.getSalary().toString()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestSalary() throws Exception {
        IncomeDto incomeDto1 = IncomeDto.builder()
                .year(2023)
                .month("JANUARY")
                .salary(-1_500_000)
                .hour_income(11029.41)
                .workdays_month(17)
                .build();

        mockMvc.perform(get("/hic/income")
                        .queryParam("year", incomeDto1.getYear().toString())
                        .queryParam("month", incomeDto1.getMonth())
                        .queryParam("salary", incomeDto1.getSalary().toString()))

                .andExpect(status().isBadRequest());
    }

}
