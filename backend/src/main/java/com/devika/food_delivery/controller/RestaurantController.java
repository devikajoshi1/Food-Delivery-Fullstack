package com.devika.food_delivery.controller;

import com.devika.food_delivery.dto.MenuItemResponse;
import com.devika.food_delivery.dto.PageResponse;
import com.devika.food_delivery.dto.RestaurantResponse;
import com.devika.food_delivery.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public PageResponse<RestaurantResponse> search(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int pageSize){
        return restaurantService.search(search, page, pageSize);
    }

    @GetMapping("/{id}")

    public RestaurantResponse getRestaurant(@PathVariable Long id){
        return  restaurantService.getRestaurant(id);
    }

    @GetMapping("/{id}/menu-item")

    public List<MenuItemResponse> getMenu(@PathVariable Long id){
        return restaurantService.getMenu(id);
    }
}
