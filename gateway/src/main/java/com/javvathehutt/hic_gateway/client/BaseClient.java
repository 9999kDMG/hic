package com.javvathehutt.hic_gateway.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequest(path, parameters);
    }

    protected ResponseEntity<Object> get(String path) {
        return makeAndSendRequest(path, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(String path, Map<String, Object> parameters) {
        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.getForEntity(path, Object.class, parameters);
            } else {
                serverResponse = rest.getForEntity(path, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareGatewayResponse(serverResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
