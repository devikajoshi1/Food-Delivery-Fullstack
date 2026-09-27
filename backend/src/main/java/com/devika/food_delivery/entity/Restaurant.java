package com.devika.food_delivery.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String cuisine;

    private String address;

    @Column(nullable = false)
    private boolean active = true;

    protected Restaurant() {
    }

    public Restaurant(String name, String cuisine, String address) {
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public String getAddress() { return address; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }


}
