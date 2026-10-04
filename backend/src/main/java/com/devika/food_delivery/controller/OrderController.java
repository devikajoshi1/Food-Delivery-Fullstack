package com.devika.food_delivery.controller;

import com.devika.food_delivery.dto.OrderResponse;
import com.devika.food_delivery.dto.PlaceOrderRequest;
import com.devika.food_delivery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PlaceOrderRequest request) {

        // The user comes from the checked token, never from the body
        Long userId = Long.valueOf(jwt.getSubject());
        OrderResponse order = orderService.placeOrder(userId, request);

        URI location = URI.create("/api/orders/" + order.id());
        return ResponseEntity.created(location).body(order);
    }
    @GetMapping("/{id}")
    public OrderResponse getOrder(@AuthenticationPrincipal Jwt jwt,
                                  @PathVariable Long id) {
        Long userId = Long.valueOf(jwt.getSubject());
        return orderService.getOrder(userId, id);
    }

}
