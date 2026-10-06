package com.devika.food_delivery.fake;

import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A stand-in for Razorpay's Orders API, so the app runs without a Razorpay
 * account. Same URL shape, same Basic auth check, same JSON fields.
 *
 * To use the real Razorpay instead, set RAZORPAY_URL to
 * https://api.razorpay.com/v1 and use real test keys. This class then
 * simply isn't called.
 */
@Hidden
@RestController
@RequestMapping("/fake-razorpay/v1")
public class FakeRazorpayController {

    private static final Logger log =
            LoggerFactory.getLogger(FakeRazorpayController.class);
    private static final String ID_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final String expectedAuth;

    public FakeRazorpayController(
            @Value("${razorpay.key-id:}") String keyId,
            @Value("${razorpay.key-secret:}") String keySecret) {
        String pair = keyId + ":" + keySecret;
        this.expectedAuth = "Basic " + Base64.getEncoder()
                .encodeToString(pair.getBytes(StandardCharsets.UTF_8));
    }

    // Razorpay's POST /v1/orders
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestHeader(value = "Authorization", required = false)
            String authorization,
            @RequestBody Map<String, Object> request) {

        if (!expectedAuth.equals(authorization)) {
            return error(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
        if (!(request.get("amount") instanceof Number amount)
                || amount.longValue() < 100) {
            return error(HttpStatus.BAD_REQUEST,
                    "The amount must be atleast INR 1.00");
        }

        Map<String, Object> order = new LinkedHashMap<>();
        order.put("id", "order_" + randomId());
        order.put("entity", "order");
        order.put("amount", amount.longValue());
        order.put("amount_paid", 0);
        order.put("amount_due", amount.longValue());
        order.put("currency", request.getOrDefault("currency", "INR"));
        order.put("receipt", request.get("receipt"));
        order.put("status", "created");
        order.put("attempts", 0);
        order.put("created_at", Instant.now().getEpochSecond());

        log.info("Fake Razorpay: created {} for {} paise",
                order.get("id"), amount.longValue());
        return ResponseEntity.ok(order);
    }

    // Razorpay's error shape: {"error": {"code": ..., "description": ...}}
    private static ResponseEntity<Map<String, Object>> error(
            HttpStatus status, String description) {
        Map<String, Object> body = Map.of("error", Map.of(
                "code", "BAD_REQUEST_ERROR",
                "description", description));
        return ResponseEntity.status(status).body(body);
    }

    // 14 letters and digits, like Razorpay's own ids
    private static String randomId() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            id.append(ID_CHARS.charAt(RANDOM.nextInt(ID_CHARS.length())));
        }
        return id.toString();
    }
}
