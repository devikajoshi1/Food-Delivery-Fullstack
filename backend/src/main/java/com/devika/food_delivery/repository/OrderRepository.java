package com.devika.food_delivery.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devika.food_delivery.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}