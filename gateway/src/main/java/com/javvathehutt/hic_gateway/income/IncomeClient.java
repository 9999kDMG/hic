package com.javvathehutt.hic_gateway.income;

import com.javvathehutt.hic_gateway.client.BaseClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class IncomeClient extends BaseClient {
    private static final String API_PREFIX = "/hic";

    public IncomeClient(@Value("${hic-server.url}") String serverUrl,
                        RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getIncome(Integer year, Integer month, Integer salary) {
        Map<String, Object> parameters = Map.of(
                "year", year,
                "month", month,
                "salary", salary
        );

        return get("/income?year={year}&month={month}&salary={salary}", parameters);
    }

    public ResponseEntity<String> getIndex() {
        ResponseEntity<String> index;
        try {
            index = rest.getForEntity("/main",
                    String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }

        return index;
    }
}
