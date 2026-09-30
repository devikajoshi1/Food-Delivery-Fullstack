package com.devika.food_delivery.repository;

import com.devika.food_delivery.entity.MenuItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantIdAndAvailableTrueOrderByNameAsc(Long restaurantId);
}
