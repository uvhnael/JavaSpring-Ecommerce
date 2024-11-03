package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.OrderItem;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean addOrderItem(int productId, int orderId, int variantId, double price, int quantity) {
        String query;
        if (variantId == 0) {
            query = "INSERT INTO order_items (product_id, order_id, price, quantity) VALUES (?, ?, ?, ?)";
        } else {
            query = "INSERT INTO order_items (product_id, order_id, variant_id, price, quantity) VALUES (?, ?, ?, ?, ?)";
        }
        return variantId == 0 ?
                jdbcTemplate.update(query, productId, orderId, price, quantity) > 0 :
                jdbcTemplate.update(query, productId, orderId, variantId, price, quantity) > 0;
    }

    public List<OrderItem> getByOrderId(int orderId) {
        String query = "SELECT * FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(query, new Object[]{orderId}, (rs, rowNum) -> new OrderItem(
                rs.getInt("id"),
                rs.getInt("product_id"),
                rs.getInt("order_id"),
                rs.getInt("variant_id"),
                rs.getDouble("price"),
                rs.getInt("quantity")

        ));
    }
}
