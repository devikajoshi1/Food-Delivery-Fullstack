package com.devika.food_delivery.dto;

import com.devika.food_delivery.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull OrderStatus status) {
}
