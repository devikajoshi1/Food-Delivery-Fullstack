package com.devika.food_delivery.dto;

// Everything React needs to open Razorpay Checkout (lesson 0014)
public record PaymentResponse(String keyId,
                              String razorpayOrderId,
                              long amount,
                              String currency) {
}
