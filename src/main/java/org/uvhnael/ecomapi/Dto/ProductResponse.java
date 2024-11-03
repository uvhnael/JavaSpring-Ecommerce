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
    private double discountPrice;
    private int quantity;
    private String description;
    private Boolean isPublished;
    private Boolean isDeleted;

    public static ProductResponse form(Product product, String imagePath, double productRating) {
        Price regularPrice = new Price(product.getRegularPrice(), "VND", String.format("%,.0f ₫", product.getRegularPrice()));
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                imagePath,
                productRating,
                regularPrice,
                product.getDiscountPrice(),
                product.getQuantity(),
                product.getDescription(),
                product.getIsPublished(),
                product.getIsDeleted()
        );

    }
}
