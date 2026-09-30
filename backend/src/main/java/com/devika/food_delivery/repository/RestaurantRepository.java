package com.devika.food_delivery.repository;

import com.devika.food_delivery.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);

}
