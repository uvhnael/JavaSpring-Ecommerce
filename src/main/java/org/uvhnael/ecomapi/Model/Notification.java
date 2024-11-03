package org.uvhnael.ecomapi.Model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
@CompoundIndex(name = "type_order_idx", def = "{'type': 1, 'order_details.order_id': 1}")
public class Notification {

    @Id
    private String id;

    private String to = "all";

    private String title;

    private String body;

    private String imageUrl;

    private NotificationType type;

    private OrderDetails orderDetails;

    private ProductDetails productDetails;

    @CreatedDate
    @Indexed(expireAfter = "30d") // TTL for auto-deletion after 30 days
    private Date createdAt = new Date();

    private Date receivedAt;


    public enum NotificationType {
        ORDER_UPDATE,
        PRODUCT_SALE,
        GENERAL,
        OTHER
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetails {
        private String orderId;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetails {
        private String productId;
        private Double salePrice;
        private Double originalPrice;
    }
}
