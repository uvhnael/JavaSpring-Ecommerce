package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uvhnael.ecomapi.Model.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private int id;
    private String productName;
    private String imagePath;
    private Price regularPrice;
    private int quantity;
    private Double rate;

    public static ProductResponse form(Product product, String imagePath) {
        Price regularPriceFormatted = new Price(product.getRegularPrice(), "VND", String.format("%,.0f ₫", product.getRegularPrice()));
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                imagePath,
                regularPriceFormatted,
                product.getQuantity(),
                product.getRate()
        );
    }
}
