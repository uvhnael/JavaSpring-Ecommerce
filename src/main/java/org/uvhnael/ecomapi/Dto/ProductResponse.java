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
    private double productRating;
    private Price regularPrice;
    private int quantity;

    public static ProductResponse form(int id, String productName, int quantity, double regularPrice, String imagePath, double productRating) {
        Price regularPriceFormatted = new Price(regularPrice, "VND", String.format("%,.0f ₫", regularPrice));
        return new ProductResponse(
                id,
                productName,
                imagePath,
                productRating,
                regularPriceFormatted,
                quantity
        );

    }
}
