package com.devika.food_delivery.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.NonNull;

import java.util.List;

public record PlaceOrderRequest(@NonNull Long restaurantId,
                                @NotBlank @Size(max = 255) String deliveryAddress,
                                @NotEmpty List<@Valid Line> items) {

    public record Line(@NotNull Long menuItemId,
                       @Min(1) @Max(20) int quantity) {
    }
}