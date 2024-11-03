package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uvhnael.ecomapi.Model.Cart;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private int id;
    private int customerId;
    private int quantity;
    private int variantId;
    private String attributeValues;
    private ProductDetailResponse product;

    public static CartResponse form(Cart cart, String attributeValues, ProductDetailResponse product) {
        return new CartResponse(
                cart.getId(),
                cart.getCustomerId(),
                cart.getQuantity(),
                cart.getVariantId(),
                attributeValues,
                product
        );
    }

}
