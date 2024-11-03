package org.uvhnael.ecomapi.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.uvhnael.ecomapi.Model.ProductRate;

import java.util.List;

public interface ProductRateRepository extends MongoRepository<ProductRate, String> {
    Boolean existsByProductId(Long productId);

    List<ProductRate> findByProductId(Long productId);

    List<ProductRate> findByCustomerId(Long customerId);

    List<ProductRate> findByOrderItemId(Long orderItemId);


}
