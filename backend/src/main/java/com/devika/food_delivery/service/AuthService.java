package com.devika.food_delivery.service;

import com.devika.food_delivery.dto.AuthResponse;
import com.devika.food_delivery.dto.LoginRequest;
import com.devika.food_delivery.dto.RegisterRequest;
import com.devika.food_delivery.entity.Role;
import com.devika.food_delivery.entity.User;
import com.devika.food_delivery.exception.BadRequestException;
import com.devika.food_delivery.exception.InvalidLoginException;
import com.devika.food_delivery.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request){
        String email = request.email().trim().toLowerCase();

        //1.one account per email
        if(userRepository.existsByEmail(email)){
            throw new BadRequestException("Email is already registered");
        }

        //2.store the hash , never the password . evryone starts as customer
        String hash = passwordEncoder.encode(request.password());
        User user = new User (request.name(),email,hash, Role.CUSTOMER);
        userRepository.save(user);

        //3.log them straight in
        return new AuthResponse(tokenService.issue(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request){
        String email = request.email().trim().toLowerCase();

        //same error for no such email and wrong pasword
        User user = userRepository.findByEmail(email)
                .filter(found -> passwordEncoder.matches(
                        request.password(),found.getPasswordHash()))
                .orElseThrow(InvalidLoginException::new);
        return new AuthResponse(tokenService.issue(user));
    }
}

