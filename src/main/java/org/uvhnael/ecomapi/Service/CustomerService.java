package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Dto.AuthBody;
import org.uvhnael.ecomapi.Dto.AuthResponse;
import org.uvhnael.ecomapi.Model.Customer;
import org.uvhnael.ecomapi.Repository.CustomerRepository;

import org.uvhnael.ecomapi.Repository.EventRepository;
import org.uvhnael.ecomapi.Repository.ProductRateRepository;
import org.uvhnael.ecomapi.Utility.JwtUtility;
import org.uvhnael.ecomapi.exception.auth.UnauthorizedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtility jwtUtility;

    private final RecommendService recommendService;

    public List<Customer> getAll() {
        return customerRepository.getAll();
    }

    public Customer getById(int id) {
        return customerRepository.getById(id);
    }

    public AuthResponse login(AuthBody authBody) {
        if (customerRepository.getByUsername(authBody.getEmail())) {
            Customer customer = customerRepository.getAllByUsername(authBody.getEmail());
            if (verifyPassword(authBody.getPassword(), customer.getPassword())) {
                String token = jwtUtility.generateToken(String.valueOf(customer.getId()), customer.getName(), customer.getEmail());
                return new AuthResponse(customer.getId(), customer.getName(), customer.getEmail(), token);
            } else {
                throw new UnauthorizedException("Username or password is incorrect");
            }
        } else {
            throw new UnauthorizedException("Username not found");
        }
    }

    public AuthResponse register(Customer customer) {
        if (customerRepository.getByUsername(customer.getEmail())) {
            throw new UnauthorizedException("Username already exists");
        } else {
            customer.setPassword(hashPassword(customer.getPassword()));
            int customerId = customerRepository.create(customer);
            if (customerId == 0) {
                throw new UnauthorizedException("Failed to register");
            }
            recommendService.addNewCustomer(customerId);
            customer.setId(customerId);
            String token = jwtUtility.generateToken(String.valueOf(customer.getId()), customer.getName(), customer.getEmail());
            return new AuthResponse(customer.getId(), customer.getName(), customer.getEmail(), token);
        }
    }

    public boolean update(Customer customer) {
        if (customerRepository.getByUsername(customer.getEmail())) {
            customerRepository.update(customer);
            return true;
        } else {
            throw new UnauthorizedException("Username not found");
        }
    }

    public boolean delete(int id) {
        if (customerRepository.getById(id) != null) {
            customerRepository.delete(id);
            return true;
        } else {
            throw new UnauthorizedException("Username not found");
        }
    }

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }


}
