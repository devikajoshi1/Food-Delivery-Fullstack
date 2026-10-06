package com.devika.food_delivery.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class RazorpayGateway {

    private final String keyId;
    private final RestClient restClient;

    public RazorpayGateway(
            @Value("${razorpay.url}") String baseUrl,
            @Value("${razorpay.key-id}") String keyId,
            @Value("${razorpay.key-secret}") String keySecret) {
        this.keyId = keyId;

        // Every call logs in with the key id and secret (HTTP Basic auth)
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers ->
                        headers.setBasicAuth(keyId, keySecret))
                .build();
    }

    // The key id is public: React needs it to open Checkout
    public String keyId() {
        return keyId;
    }

    // POST /v1/orders. Razorpay answers with an id like "order_RB58MiP5SPFYyM"
    public RazorpayOrder createOrder(long amountInPaise, String receipt) {
        Map<String, Object> body = Map.of(
                "amount", amountInPaise,
                "currency", "INR",
                "receipt", receipt);

        return restClient.post()
                .uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(RazorpayOrder.class);
    }

    // Only the fields we use. Razorpay sends more, and they're ignored
    public record RazorpayOrder(String id, long amount, String currency) {
    }
}
