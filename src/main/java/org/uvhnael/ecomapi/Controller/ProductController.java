package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uvhnael.ecomapi.Dto.*;
import org.uvhnael.ecomapi.Model.Event;
import org.uvhnael.ecomapi.Service.*;
import org.uvhnael.ecomapi.Utility.JwtUtility;
import org.uvhnael.ecomapi.exception.attribute.AttributeCreationException;
import org.uvhnael.ecomapi.exception.category.CategoryNotFoundException;
import org.uvhnael.ecomapi.exception.gallery.GalleryNotFoundException;
import org.uvhnael.ecomapi.exception.product.ProductCreationException;
import org.uvhnael.ecomapi.exception.product.ProductNotFoundException;
import org.uvhnael.ecomapi.exception.variant.VariantCreationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final JwtUtility jwtUtil;


    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<ProductResponse> products = productService.getProducts(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/random")
    public ResponseEntity<?> getRandomProducts(
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<ProductResponse> products = productService.getRandomProducts(size);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/recommend/customer/{customerId}")
    public ResponseEntity<?> getRecommendProduct(
            @PathVariable int customerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        try {
            List<ProductResponse> products = productService.getRecommendProduct(customerId, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @PathVariable int id) {
        String customerId = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Bỏ qua "Bearer "
            System.out.println("Token: " + token);
            customerId = jwtUtil.extractId(token);
        }

        try {
            ProductDetailResponse product = productService.viewProduct(id, Integer.parseInt(customerId));
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Product not found", e.getMessage()));
        } catch (GalleryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Gallery not found", e.getMessage()));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Category not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }


    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword) {
        try {
            List<ProductResponse> products = productService.searchProducts(keyword);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getByCategory(@PathVariable int categoryId) {
        try {

            List<ProductResponse> products = productService.getByCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Category not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductBody product) {
        try {
            boolean result = productService.createProduct(product);
            if (result) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Product creation failed", "Product creation failed"));
            }
        } catch (ProductCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Product creation failed", e.getMessage()));
        } catch (AttributeCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Attribute creation failed", e.getMessage()));
        } catch (VariantCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Variant creation failed", e.getMessage()));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Category not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }


    private Object createErrorResponse(String error, String message) {
        return new ErrorResponse(error, message);
    }

}
