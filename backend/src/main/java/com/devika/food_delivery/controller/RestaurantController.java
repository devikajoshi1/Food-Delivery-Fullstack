package com.devika.food_delivery.controller;

import com.devika.food_delivery.dto.MenuItemResponse;
import com.devika.food_delivery.dto.PageResponse;
import com.devika.food_delivery.dto.RestaurantResponse;
import com.devika.food_delivery.service.RestaurantService;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public PageResponse<RestaurantResponse> search(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize){
        return restaurantService.search(search, page, pageSize);
    }

    @GetMapping("/{id}")

    public RestaurantResponse getRestaurant(@PathVariable Long id){
        return  restaurantService.getRestaurant(id);
    }

    @GetMapping("/{id}/menu-items")

    public List<MenuItemResponse> getMenu(@PathVariable Long id){
        return restaurantService.getMenu(id);
    }
}
