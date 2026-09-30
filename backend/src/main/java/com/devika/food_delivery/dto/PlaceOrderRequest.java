package com.devika.food_delivery.dto;


import java.util.List;

public record PlaceOrderRequest(Long restaurantId, String deliveryAddress, List<Line> items) {

    public record Line(Long menuItemId, int quantity) {
    }
}