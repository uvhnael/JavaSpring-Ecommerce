package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private int id;
    private int productId;
    private int orderId;
    private int variantId;
    private double price;
    private int quantity;
    private int couponId;

    public OrderItem(int id, int productId, int orderId, int variantId, double price, int quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.variantId = variantId;
        this.price = price;
        this.quantity = quantity;
    }
}
