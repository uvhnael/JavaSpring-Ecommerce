package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.uvhnael.ecomapi.Dto.CustomerRecommend;
import org.uvhnael.ecomapi.Dto.ErrorResponse;
import org.uvhnael.ecomapi.Dto.ProductRecommend;
import org.uvhnael.ecomapi.Service.CustomerService;
import org.uvhnael.ecomapi.Service.ProductRateService;
import org.uvhnael.ecomapi.Service.ProductService;

import java.util.List;

@Controller
@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final ProductService productService;
    private final CustomerService customerService;
    private final ProductRateService productRateService;

    @GetMapping("/products")
    public ResponseEntity<?> getRecommendProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        try {
            List<ProductRecommend> products = productService.getProductsData(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getRecommendCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        try {
            List<CustomerRecommend> customers = customerService.getCustomersData(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }


    private Object createErrorResponse(String error, String message) {
        return new ErrorResponse(error, message);
    }


}
