package com.devika.food_delivery.service;

import com.devika.food_delivery.dto.OrderResponse;
import com.devika.food_delivery.dto.PaymentResponse;
import com.devika.food_delivery.service.RazorpayGateway.RazorpayOrder;
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
    private  final MenuItemRepository menuItemRepository;
    private final RazorpayGateway razorpayGateway;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        RestaurantRepository restaurantRepository,
                        MenuItemRepository menuItemRepository,
                        RazorpayGateway razorpayGateway) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.razorpayGateway = razorpayGateway;

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
    public OrderResponse getOrder(Long userId, Long orderId){
        return OrderResponse.from(findOwnOrder(userId, orderId));
    }

    @Transactional
    public PaymentResponse startPayment(Long userId, Long orderId) {
        Order order = findOwnOrder(userId, orderId);
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BadRequestException(
                    "Order " + orderId + " is not waiting for payment");
        }

        // Razorpay counts in paise: ₹90.00 becomes 9000
        long amountInPaise = order.getTotalAmount()
                .movePointRight(2)
                .longValueExact();

        // A new Razorpay order for every attempt, so a new receipt too
        String receipt = "order-" + orderId + "-" + System.currentTimeMillis();
        RazorpayOrder razorpayOrder =
                razorpayGateway.createOrder(amountInPaise, receipt);

        // Remember it: lesson 0014 checks the payment against this id
        order.setRazorpayOrderId(razorpayOrder.id());
        orderRepository.save(order);

        return new PaymentResponse(
                razorpayGateway.keyId(),
                razorpayOrder.id(),
                razorpayOrder.amount(),
                razorpayOrder.currency());
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
}
