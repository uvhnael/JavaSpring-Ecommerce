package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.Variant;
import org.uvhnael.ecomapi.Repository.VariantRepository;
import org.uvhnael.ecomapi.exception.variant.VariantCreationException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VariantService {

    private final VariantRepository variantRepository;

    public List<Variant> getByProductId(int productId) {
        return variantRepository.getByProductId(productId);
    }

    public void createVariants(List<Variant> variants, int productId) {
        for (Variant variant : variants) {
            if (!variantRepository.createVariant(productId, variant.getPrice(), variant.getQuantity())) {
                throw new VariantCreationException("Variant creation failed");
            }
        }
    }
}
