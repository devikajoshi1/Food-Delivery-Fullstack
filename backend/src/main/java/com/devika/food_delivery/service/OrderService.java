package com.devika.food_delivery.service;

import com.devika.food_delivery.dto.OrderResponse;
import com.devika.food_delivery.dto.PlaceOrderRequest;
import com.devika.food_delivery.entity.*;
import com.devika.food_delivery.exception.BadRequestException;
import com.devika.food_delivery.exception.NotFoundException;
import com.devika.food_delivery.repository.MenuItemRepository;
import com.devika.food_delivery.repository.OrderRepository;
import com.devika.food_delivery.repository.RestaurantRepository;
import com.devika.food_delivery.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        RestaurantRepository restaurantRepository,
                        MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;

    }

    @Transactional
    public OrderResponse placeOrder(Long userId, PlaceOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .filter(Restaurant::isActive)
                .orElseThrow(() -> new NotFoundException("Restaurant " + request.restaurantId() + " not found"));

        Order order = new Order(user, restaurant, request.deliveryAddress());
        BigDecimal total = BigDecimal.ZERO;

        for (PlaceOrderRequest.Line line : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(line.menuItemId())
                    .orElseThrow(() -> new NotFoundException("Menu item " + line.menuItemId() + " not found"));
            if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                throw new BadRequestException("All items must come from the same restaurant");
            }
            if (!menuItem.isAvailable()) {
                throw new BadRequestException(menuItem.getName() + " is not available right now");
            }

            BigDecimal price = menuItem.getPrice();
            order.addItem(new OrderItem(menuItem, line.quantity(), price));
            total = total.add(price.multiply(BigDecimal.valueOf(line.quantity())));
        }

        order.setTotalAmount(total);
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long userId, Long orderId) {
        return OrderResponse.from(findOwnOrder(userId, orderId));
    }


    //pay
    @Transactional
    public OrderResponse pay(long userId, Long orderId, PaymentMethod method) {
        Order order = findOwnOrder(userId, orderId);
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BadRequestException(
                    "Order " + orderId + " is alredy paid");
        }
        order.setPaymentMethod(method);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    //findby
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }


    // Someone else's order is "not found": don't even admit it exists
    private Order findOwnOrder(Long userId, Long orderId) {
        return orderRepository.findById(orderId)
                .filter(found -> found.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException(
                        "Order " + orderId + " not found"));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "createdAt");
        return orderRepository.findAll(newestFirst).stream()
                .map(OrderResponse::from)
                .toList();
    }

    //admin
    @Transactional
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(
                        "Order " + orderId + " not found"
                ));

        if (newStatus == OrderStatus.PENDING_PAYMENT
                || newStatus == OrderStatus.PAID) {
            throw new BadRequestException(
                    "Only a payment can set " + newStatus);
        }
        // Rule 2: an unpaid order can be cancelled, but not cooked
        if (order.getStatus() == OrderStatus.PENDING_PAYMENT
                && newStatus != OrderStatus.CANCELED) {
            throw new BadRequestException(
                    "Order " + orderId + " is not paid yet");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
        return OrderResponse.from(order);

    }
}