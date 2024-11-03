package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Model.ProductRate;
import org.uvhnael.ecomapi.Repository.ProductRateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRateService {

    private final ProductRateRepository productRateRepository;

    public List<ProductRate> findByProductId(Long productId) {
        return productRateRepository.findByProductId(productId);
    }

    public ProductRate createProductRate(ProductRate productRate) {
        return productRateRepository.save(productRate);
    }

    public List<ProductRate> findByCustomerId(Long customerId) {
        return productRateRepository.findByCustomerId(customerId);
    }

    public void saveAll(List<ProductRate> productRates) {
        productRateRepository.saveAll(productRates);
    }

}
