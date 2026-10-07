package com.devika.food_delivery.dto;

import com.devika.food_delivery.entity.Order;
import com.devika.food_delivery.entity.OrderItem;
import com.devika.food_delivery.entity.OrderStatus;
import com.devika.food_delivery.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        PaymentMethod paymentMethod,
        String restaurantName,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<Line> items) {

    public record Line(String name, int quantity, BigDecimal unitPrice) {

        static Line from(OrderItem item) {
            return new Line(item.getMenuItem().getName(), item.getQuantity(), item.getUnitPrice());
        }
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getRestaurant().getName(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getItems().stream().map(Line::from).toList());
    }
}
