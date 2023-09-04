package com.javvathehutt.hic_gateway;

import com.javvathehutt.hic_gateway.exception.ErrorHandler;
import com.javvathehutt.hic_gateway.income.IncomeController;
import com.javvathehutt.hic_gateway.income.dto.IncomeDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
public class IncomeControllerIntegrationTest {
    private MockMvc mockMvc;
    private final IncomeController incomeController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(incomeController)
                .setControllerAdvice(ErrorHandler.class)
                .build();
    }

    @Test
    void shouldReturnBadRequestException() throws Exception {
        IncomeDto incomeDto1 = IncomeDto.builder()
                .year(20023)
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
}
