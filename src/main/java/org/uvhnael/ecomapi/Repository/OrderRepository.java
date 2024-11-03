package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.Order;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public int createOrder(double price, int couponId, int customerId, int customerAddressId) {
        String query = "INSERT INTO orders (price, coupon_id, customer_id, customer_address_id, order_status_id) VALUES (?, ?, ?, ?, 1)";
        jdbcTemplate.update(query, price, couponId == 0 ? null : couponId, customerId, customerAddressId);
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        String query = "SELECT * FROM orders WHERE customer_id = ?";
        return jdbcTemplate.query(query, new Object[]{customerId}, (rs, rowNum) -> new Order(
                rs.getInt("id"),
                rs.getDouble("price"),
                rs.getInt("coupon_id"),
                rs.getInt("customer_id"),
                rs.getInt("customer_address_id"),
                rs.getInt("order_status_id"),
                rs.getString("order_approved_at"),
                rs.getString("order_delivered_carrier_date"),
                rs.getString("order_delivered_customer_date"),
                rs.getString("created_at")
        ));
    }

    public Order getById(int id) {
        String query = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> new Order(
                rs.getInt("id"),
                rs.getDouble("price"),
                rs.getInt("coupon_id"),
                rs.getInt("customer_id"),
                rs.getInt("customer_address_id"),
                rs.getInt("order_status_id"),
                rs.getString("order_approved_at"),
                rs.getString("order_delivered_carrier_date"),
                rs.getString("order_delivered_customer_date"),
                rs.getString("created_at")
        ));
    }


}
