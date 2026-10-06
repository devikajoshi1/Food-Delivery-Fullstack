package com.devika.food_delivery.controller;

import com.devika.food_delivery.dto.AuthResponse;
import com.devika.food_delivery.dto.LoginRequest;
import com.devika.food_delivery.dto.RegisterRequest;
import com.devika.food_delivery.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
// Swagger: login and register never send the Authorize token
@SecurityRequirements
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request){
        return authService.login(request);
    }
}
