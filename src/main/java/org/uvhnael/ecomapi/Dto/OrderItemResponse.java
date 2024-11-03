package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uvhnael.ecomapi.Model.OrderItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private int id;
    private int productId;
    private String productName;
    private String imagePath;
    private int orderId;
    private int variantId;
    private String attributeValues;
    private double price;
    private int quantity;
    private int couponId;

    public static OrderItemResponse from(OrderItem orderItem, String productName, String imagePath, String attributeValues) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProductId(),
                productName,
                imagePath,
                orderItem.getOrderId(),
                orderItem.getVariantId(),
                attributeValues,
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getCouponId()
        );
    }
}


