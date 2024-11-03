package org.uvhnael.ecomapi.exception.coupon;

public class CouponExpiredException extends RuntimeException {
    public CouponExpiredException(String message) {
        super(message);
    }
}

