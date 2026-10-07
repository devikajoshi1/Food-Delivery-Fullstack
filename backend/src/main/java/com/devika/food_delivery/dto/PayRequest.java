package com.devika.food_delivery.dto;

import com.devika.food_delivery.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PayRequest(@NotNull PaymentMethod method) {

}
