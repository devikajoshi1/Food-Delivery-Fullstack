package com.devika.food_delivery.controller;

import com.devika.food_delivery.dto.OrderResponse;
import com.devika.food_delivery.dto.UpdateStatusRequest;
import com.devika.food_delivery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/orders/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request){
        return orderService.updateStatus(id, request.status());
    }
}
