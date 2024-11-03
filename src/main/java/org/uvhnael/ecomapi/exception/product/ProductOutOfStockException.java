package org.uvhnael.ecomapi.exception.product;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException(String message) {
        super(message);
    }

    public ProductOutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}