package com.devika.food_delivery.service;

import com.devika.food_delivery.dto.MenuItemResponse;
import com.devika.food_delivery.dto.PageResponse;
import com.devika.food_delivery.dto.RestaurantResponse;
import com.devika.food_delivery.entity.Restaurant;
import com.devika.food_delivery.exception.NotFoundException;
import com.devika.food_delivery.repository.MenuItemRepository;
import com.devika.food_delivery.repository.RestaurantRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private static final int MAX_PAGE_SIZE = 50;

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public PageResponse<RestaurantResponse> search(String search, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE), Sort.by("name"));
        return PageResponse.from(
                restaurantRepository.findByActiveTrueAndNameContainingIgnoreCase(search, pageRequest),
                RestaurantResponse::from);
    }

    public RestaurantResponse getRestaurant(Long id) {
        return RestaurantResponse.from(findActiveRestaurant(id));
    }

    public List<MenuItemResponse> getMenu(Long restaurantId) {
        findActiveRestaurant(restaurantId);
        return menuItemRepository.findByRestaurantIdAndAvailableTrueOrderByNameAsc(restaurantId).stream()
                .map(MenuItemResponse::from)
                .toList();
    }

    private Restaurant findActiveRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .filter(Restaurant::isActive)
                .orElseThrow(() -> new NotFoundException("Restaurant " + id + " not found"));

    }
}
