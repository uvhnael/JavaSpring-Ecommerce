package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uvhnael.ecomapi.Model.ProductRate;
import org.uvhnael.ecomapi.Service.ProductRateService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/rates")
@RequiredArgsConstructor
public class ProductRateController {

    private final ProductRateService productRateService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getByProductId(@PathVariable Long productId) {
        List<ProductRate> rates = productRateService.findByProductId(productId);
        if (rates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ratings found for product ID: " + productId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(rates);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getByCustomerId(@PathVariable Long customerId) {
        List<ProductRate> rates = productRateService.findByCustomerId(customerId);
        if (rates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ratings found for customer ID: " + customerId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(rates);
    }

    @PostMapping
    public ResponseEntity<?> createProductRate(@RequestBody ProductRate productRate) {
        ProductRate rate = productRateService.createProductRate(productRate);
        return ResponseEntity.status(HttpStatus.CREATED).body(rate);
    }
}
