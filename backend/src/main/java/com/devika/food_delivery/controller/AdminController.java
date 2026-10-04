package com.devika.food_delivery.controller;

import com.devika.food_delivery.dto.OrderResponse;
import com.devika.food_delivery.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final OrderService orderService;

    public AdminController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<OrderResponse> allOders(){
        return orderService.getAllOrders();
    }
}
