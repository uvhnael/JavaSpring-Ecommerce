package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uvhnael.ecomapi.Model.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private int id;
    private String productName;
    private Price regularPrice;
    private double discountPrice;
    private int quantity;
    private String description;
    private Boolean isPublished;
    private Boolean isDeleted;
    private List<String> attributes;
    private List<List<String>> attributeValues;
    private List<Variant> variants;
    private List<String> galleries;
    private Category category;

    public static ProductDetailResponse form(Product product, List<Attribute> attributes, List<Variant> variants, List<Gallery> galleries, Category category) {
        List<String> attributeNames = new ArrayList<>();
        List<List<String>> attributeValues = new ArrayList<>();
        for (Attribute attribute : attributes) {
            attributeNames.add(attribute.getAttributeName());
            List<String> values = new ArrayList<>();
            for (AttributeValue attributeValue : attribute.getAttributeValues()) {
                values.add(attributeValue.getAttributeValue());
            }
            attributeValues.add(values);
        }

        List<String> galleryPaths = new ArrayList<>();
        for (Gallery gallery : galleries) {
            galleryPaths.add(gallery.getImagePath());
        }
        Price regularPrice = new Price(product.getRegularPrice(), "VND", String.format("%,.0f₫", product.getRegularPrice()));


        return new ProductDetailResponse(
                product.getId(),
                product.getProductName(),
                regularPrice,
                product.getDiscountPrice(),
                product.getQuantity(),
                product.getDescription(),
                product.getIsPublished(),
                product.getIsDeleted(),
                attributeNames,
                attributeValues,
                variants,
                galleryPaths,
                category
        );
    }
}
