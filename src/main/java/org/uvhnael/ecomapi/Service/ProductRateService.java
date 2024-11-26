package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Dto.ProductRateResponse;
import org.uvhnael.ecomapi.Model.ProductRate;
import org.uvhnael.ecomapi.Repository.ProductRateRepository;

import java.util.ArrayList;
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

    public ProductRateResponse countRateForProduct() {
        List<ProductRate> rates = productRateRepository.findAll();
        ProductRateResponse count;

        List<Long> productId = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();

        for (ProductRate rate : rates) {
            if (!productId.contains(rate.getProductId())) {
                productId.add(rate.getProductId());
                countList.add(1);
            } else {
                int index = productId.indexOf(rate.getProductId());
                countList.set(index, countList.get(index) + 1);
            }
        }

        count = new ProductRateResponse(productId, countList);

        count.sortByCount();

        return count;
    }

}
