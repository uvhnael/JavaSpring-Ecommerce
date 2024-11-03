package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uvhnael.ecomapi.Dto.ErrorResponse;
import org.uvhnael.ecomapi.Model.Coupon;
import org.uvhnael.ecomapi.Service.CouponService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<?> getAllCoupons() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(couponService.getAllCoupons());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getByProductId(@PathVariable int productId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(couponService.getByProductId(productId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/products/{productIds}")
    public ResponseEntity<?> getByProductIds(@PathVariable List<Integer> productIds) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(couponService.getByProductIds(productIds));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/order")
    public ResponseEntity<?> getOrderCoupons() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(couponService.getOrderCoupons());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon) {
        try {
            boolean success = couponService.createCoupon(coupon);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Coupon creation error", "Coupon not created"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Coupon created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Coupon creation error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon) {
        try {
            boolean success = couponService.updateCoupon(coupon);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Coupon update error", "Coupon not updated"));
            }
            return ResponseEntity.status(HttpStatus.OK).body("Coupon updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Coupon update error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable int id) {
        try {
            boolean success = couponService.deleteCoupon(id);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Coupon deletion error", "Coupon not deleted"));
            }
            return ResponseEntity.status(HttpStatus.OK).body("Coupon deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    private Object createErrorResponse(String error, String message) {
        return new ErrorResponse(error, message);
    }
}
