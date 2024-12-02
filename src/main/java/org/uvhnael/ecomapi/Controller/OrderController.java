package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uvhnael.ecomapi.Dto.ErrorResponse;
import org.uvhnael.ecomapi.Model.Order;
import org.uvhnael.ecomapi.Service.OrderService;
import org.uvhnael.ecomapi.exception.coupon.CouponExpiredException;
import org.uvhnael.ecomapi.exception.coupon.CouponNotFoundException;
import org.uvhnael.ecomapi.exception.coupon.CouponUsageLimitExceededException;
import org.uvhnael.ecomapi.exception.order.OrderCreationException;
import org.uvhnael.ecomapi.exception.order.OrderItemException;
import org.uvhnael.ecomapi.exception.product.ProductOutOfStockException;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getByCustomerId(@PathVariable int customerId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getByCustomerId(customerId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }


    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            boolean success = orderService.createOrder(order);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Order creation error", "Order not created"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        } catch (OrderCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Order creation error", e.getMessage()));
        } catch (OrderItemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Order item error", e.getMessage()));
        } catch (ProductOutOfStockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createErrorResponse("Product out of stock", e.getMessage()));
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Coupon not found", e.getMessage()));
        } catch (CouponUsageLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Coupon usage limit exceeded", e.getMessage()));
        } catch (CouponExpiredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Coupon expired", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid input", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    private Object createErrorResponse(String error, String message) {
        return new ErrorResponse(error, message);
    }
}


