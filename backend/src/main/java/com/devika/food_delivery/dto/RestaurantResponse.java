package com.devika.food_delivery.dto;

import com.devika.food_delivery.entity.Restaurant;

public record RestaurantResponse(Long id, String name, String cuisine, String address) {
    public static RestaurantResponse from (Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCuisine(),
                restaurant.getAddress());
    }
}
