package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String productName;
    private double regularPrice;
    private double discountPrice;
    private int quantity;
    private String description;
    private Boolean isPublished;
    private Boolean isDeleted;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;

    public Product(int id, String productName, double regularPrice, int quantity) {
        this.id = id;
        this.productName = productName;
        this.regularPrice = regularPrice;
        this.quantity = quantity;

    }
}
