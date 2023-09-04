package com.javvathehutt.hic_server.isdayof_client;

import com.javvathehutt.hic_server.exception.ReadDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;
import java.util.Optional;

@Service
public class IsdayofClient {
    private static final String API_PREFIX = "/getdata";
    private static final String REGION = "ru";
    private static final String DELIMETER = "%0A";
    private static final String REDUCED_HOURS = "1";
    private final RestTemplate rest;

    @Autowired
    public IsdayofClient(@Value("${isdayof-server.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    @Cacheable(cacheNames = "workDayMonth")
    public String getFromIsdayof(Integer year, Integer month) {
        Map<String, Object> parameters = Map.of(
                "year", year,
                "month", month,
                "cc", REGION,
                "pre", REDUCED_HOURS,
                "delimeter", DELIMETER
        );
        Optional<String> responseBody = Optional.empty();
        ResponseEntity<String> serverResponse;
        try {
            serverResponse = rest.getForEntity("?year={year}&month={month}&cc={cc}&pre={pre}&delimeter={delimeter}",
                    String.class, parameters);
        } catch (HttpStatusCodeException e) {
            return e.getMessage();
        }

        if (serverResponse.getStatusCode().is2xxSuccessful() && serverResponse.hasBody()) {
            responseBody = Optional.ofNullable(String.valueOf(serverResponse.getBody()));
        } else if (serverResponse.getStatusCode().is4xxClientError()) {
            throw new HttpClientErrorException(serverResponse.getStatusCode());
        } else if (serverResponse.getStatusCode().is5xxServerError()) {
            throw new HttpServerErrorException(serverResponse.getStatusCode());
        }

        if (responseBody.isPresent()) {
            return responseBody.get();
        } else {
            throw new ReadDataException("error receiving data from an information resource");
        }
    }
}
