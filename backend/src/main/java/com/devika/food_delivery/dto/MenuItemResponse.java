package com.devika.food_delivery.dto;

import com.devika.food_delivery.entity.MenuItem;

import java.math.BigDecimal;

public record MenuItemResponse(Long id, String name, String description, BigDecimal price) {
    public static MenuItemResponse from (MenuItem menuItem){
        return new MenuItemResponse(menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice());
    }
}
