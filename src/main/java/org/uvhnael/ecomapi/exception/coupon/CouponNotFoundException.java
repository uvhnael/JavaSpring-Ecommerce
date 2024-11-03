package org.uvhnael.ecomapi.exception.coupon;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String message) {
        super(message);
    }
}
