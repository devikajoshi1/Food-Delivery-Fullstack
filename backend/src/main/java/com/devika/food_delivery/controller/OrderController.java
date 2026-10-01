package com.devika.food_delivery.controller;

import com.devika.food_delivery.dto.OrderResponse;
import com.devika.food_delivery.dto.PlaceOrderRequest;
import com.devika.food_delivery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Long DEMO_USER_ID = 1L;

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request){
        OrderResponse order = orderService.placeOrder(DEMO_USER_ID, request);

        //201 created , plus a location heder

        URI location = URI.create("/api/orders/" + order.id());

        return ResponseEntity.created(location).body(order);
    }
    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id){
        return orderService.getOrder(DEMO_USER_ID,id);
    }

}
