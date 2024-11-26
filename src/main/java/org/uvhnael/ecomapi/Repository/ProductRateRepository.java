package org.uvhnael.ecomapi.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.uvhnael.ecomapi.Model.ProductRate;

import java.util.List;
import java.util.Optional;

public interface ProductRateRepository extends MongoRepository<ProductRate, String> {
    Boolean existsByProductId(Long productId);

    List<ProductRate> findByProductId(Long productId);

    List<ProductRate> findByCustomerId(Long customerId);

    Page<ProductRate> findAll(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { productId: ?0 } }",
            "{ $group: { _id: null, avgRate: { $avg: '$rate' } } }"
    })
    Optional<Double> getAverageRateByProductId(Long productId);
}
