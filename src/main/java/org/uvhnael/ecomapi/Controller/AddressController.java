package org.uvhnael.ecomapi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uvhnael.ecomapi.Dto.ErrorResponse;
import org.uvhnael.ecomapi.Model.Address;
import org.uvhnael.ecomapi.Service.AddressService;
import org.uvhnael.ecomapi.exception.address.AddressNotFoundException;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getByCustomerId(@PathVariable int customerId) {
        try {
            return ResponseEntity.status(200).body(addressService.getByCustomerId(customerId));
        } catch (AddressNotFoundException e) {
            return ResponseEntity.status(404).body(createErrorResponse("Address not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/customer/{customerId}/default")
    public ResponseEntity<?> getByCustomerIdAndIsDefault(@PathVariable int customerId) {
        try {
            return ResponseEntity.status(200).body(addressService.getByCustomerIDAndIsDefault(customerId));
        } catch (AddressNotFoundException e) {
            return ResponseEntity.status(404).body(createErrorResponse("Address not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody Address address) {
        try {
            boolean success = addressService.createAddress(address);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Address creation error", "Address not created"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Address created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Address creation error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody Address address) {
        try {
            boolean success = addressService.updateAddress(address);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Address update error", "Address not updated"));
            }
            return ResponseEntity.status(HttpStatus.OK).body("Address updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Address update error", e.getMessage()));
        } catch (AddressNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Address not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable int id) {
        try {
            boolean success = addressService.deleteAddress(id);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Address deletion error", "Address not deleted"));
            }
            return ResponseEntity.status(HttpStatus.OK).body("Address deleted successfully");
        } catch (AddressNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Address not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @PutMapping("{id}/customer/{customerId}")
    public ResponseEntity<?> setDefaultAddress(@PathVariable int id, @PathVariable int customerId) {
        try {
            boolean success = addressService.setDefaultAddress(id, customerId);
            if (!success) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Address set default error", "Address not set default"));
            }
            return ResponseEntity.status(HttpStatus.OK).body("Address set default successfully");
        } catch (AddressNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Address not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/city")
    public ResponseEntity<?> getCity() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(addressService.getCity());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/district/{city}")
    public ResponseEntity<?> getDistrict(@PathVariable int city) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(addressService.getDistrict(city));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/ward/{district}")
    public ResponseEntity<?> getWard(@PathVariable int district) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(addressService.getWard(district));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Unexpected error", e.getMessage()));
        }
    }

    private Object createErrorResponse(String error, String message) {
        return new ErrorResponse(error, message);
    }
}
