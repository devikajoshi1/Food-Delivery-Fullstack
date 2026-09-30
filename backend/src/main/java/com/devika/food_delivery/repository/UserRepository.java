package com.devika.food_delivery.repository;


import com.devika.food_delivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
