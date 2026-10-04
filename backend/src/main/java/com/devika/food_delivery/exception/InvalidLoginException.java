package com.devika.food_delivery.exception;

public class InvalidLoginException extends  RuntimeException{

    public InvalidLoginException(){
        super("Wrong email or password");
    }
}
