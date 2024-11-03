package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uvhnael.ecomapi.Dto.ErrorResponse;
import org.uvhnael.ecomapi.Model.Customer;
import org.uvhnael.ecomapi.Service.CustomerService;
import org.uvhnael.ecomapi.exception.auth.UnauthorizedException;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(customerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return ResponseEntity.status(200).body(customerService.getById(id));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Customer customer) {
        try {
            boolean success = customerService.update(customer);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Customer update error", "Customer not updated"));
            }
            return ResponseEntity.status(HttpStatus.OK).body("Customer updated successfully");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Unauthorized", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    private Object createErrorResponse(String error, String message) {
        return new ErrorResponse(error, message);
    }
}
