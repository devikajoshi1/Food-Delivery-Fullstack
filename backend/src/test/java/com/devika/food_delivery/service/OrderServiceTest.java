package com.devika.food_delivery.service;

import com.devika.food_delivery.entity.Order;
import com.devika.food_delivery.entity.OrderStatus;
import com.devika.food_delivery.exception.BadRequestException;
import com.devika.food_delivery.repository.MenuItemRepository;
import com.devika.food_delivery.repository.OrderRepository;
import com.devika.food_delivery.repository.RestaurantRepository;
import com.devika.food_delivery.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    // Fake repositories: no database. Each test says what they return
    @Mock
    OrderRepository orderRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    MenuItemRepository menuItemRepository;

    // A real OrderService, built with the four fakes above
    @InjectMocks
    OrderService orderService;

    @Test
    void anUnpaidOrderCannotBePrepared() {
        // Given: order 7 exists and isn't paid (a new Order starts unpaid)
        Order order = new Order(null, null, "12 Lesson Street, Pune");
        when(orderRepository.findById(7L)).thenReturn(Optional.of(order));

        // When the admin moves it to PREPARING, then it's refused...
        assertThatThrownBy(
                () -> orderService.updateStatus(7L, OrderStatus.PREPARING))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Order 7 is not paid yet");

        // ...and nothing was saved
        verify(orderRepository, never()).save(any());
    }

    @Test
    void onlyAPaymentCanSetPaid() {
        Order order = new Order(null, null, "12 Lesson Street, Pune");
        when(orderRepository.findById(7L)).thenReturn(Optional.of(order));

        assertThatThrownBy(
                () -> orderService.updateStatus(7L, OrderStatus.PAID))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Only a payment can set PAID");

        verify(orderRepository, never()).save(any());
    }
}
