package com.company.interview.exception.order;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderID) {
        super("Could not find order with id = " + orderID);
    }
}
