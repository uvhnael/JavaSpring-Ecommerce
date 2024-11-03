package org.uvhnael.ecomapi.exception.order;

public class OrderItemException extends RuntimeException {
    public OrderItemException(String message) {
        super(message);
    }

    public OrderItemException(String message, Throwable cause) {
        super(message, cause);
    }
}

