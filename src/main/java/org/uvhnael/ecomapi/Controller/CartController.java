package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uvhnael.ecomapi.Dto.CartBody;
import org.uvhnael.ecomapi.Dto.ErrorResponse;
import org.uvhnael.ecomapi.Model.Cart;
import org.uvhnael.ecomapi.Service.CartService;
import org.uvhnael.ecomapi.exception.cart.CartNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getCart(@PathVariable int id) {
        return ResponseEntity.status(200).body(cartService.getCart(id));
    }

    @GetMapping("/order/{cartIds}")
    public ResponseEntity<?> getCartForOrder(@PathVariable List<Integer> cartIds) {
        try {
            return ResponseEntity.status(200).body(cartService.getCartForOrder(cartIds));
        } catch (CartNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Cart not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody Cart cart) {
        try {
            if (cartService.addToCart(cart)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Added to cart");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Cart add error", "Cart not added"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateCartQuantity(@RequestBody Cart cart) {
        try {
            if (cartService.updateCart(cart))
                return ResponseEntity.status(HttpStatus.OK).body("Cart Updated");
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Cart update error", "Cart not updated"));
        } catch (CartNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Cart not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable int id) {
        if (cartService.removeFromCart(id))
            return ResponseEntity.status(200).body("200 OK");
        else
            return ResponseEntity.status(500).body("500 Internal Server Error");
    }

    private Object createErrorResponse(String error, String message) {
        return new ErrorResponse(error, message);
    }

}
