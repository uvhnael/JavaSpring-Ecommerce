package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.Cart;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean addToCart(int customerId, int productId, int quantity, int variantId) {
        String query = "INSERT INTO carts (customer_id, product_id, quantity, variant_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(query, customerId, productId, quantity, variantId) > 0;
    }

    public boolean addToCart(int customerId, int productId, int quantity) {
        String query = "INSERT INTO carts (customer_id, product_id, quantity) VALUES (?, ?, ?)";
        return jdbcTemplate.update(query, customerId, productId, quantity) > 0;
    }

    public boolean removeFromCart(int customerId, int productId, int variantId) {
        if (variantId == 0) {
            String query = "DELETE FROM carts WHERE product_id = ? AND customer_id = ?";
            return jdbcTemplate.update(query, productId, customerId) > 0;
        }
        String query = "DELETE FROM carts WHERE product_id = ? AND variant_id = ? AND customer_id = ?";
        return jdbcTemplate.update(query, productId, variantId, customerId) > 0;
    }

    public boolean removeFromCart(int id) {
        String query = "DELETE FROM carts WHERE id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }

    public boolean updateCart(Cart cart) {
        String query = "UPDATE carts SET quantity = ?, variant_id = ? WHERE id = ?";
        return jdbcTemplate.update(query, cart.getQuantity(), cart.getVariantId(), cart.getId()) > 0;
    }

    public boolean clearCart(int customerId) {
        String query = "DELETE FROM carts WHERE customer_id = ?";
        return jdbcTemplate.update(query, customerId) > 0;
    }

    public List<Cart> getCartByCustomerId(int customerId) {
        String query = "SELECT * FROM carts WHERE customer_id = ?";
        return jdbcTemplate.query(query, new Object[]{customerId}, (rs, rowNum) -> new Cart(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity"),
                rs.getInt("variant_id")
        ));
    }

    public Cart isExist(int customerId, int productId, int variantId) {
        String query = "SELECT * FROM carts WHERE customer_id = ? AND product_id = ? AND variant_id = ?";
        int rows = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM carts WHERE customer_id = ? AND product_id = ? AND variant_id = ?", new Object[]{customerId, productId, variantId}, Integer.class);

        if (rows == 0) {
            return null;
        }

        return jdbcTemplate.queryForObject(query, new Object[]{customerId, productId, variantId}, (rs, rowNum) -> new Cart(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity"),
                rs.getInt("variant_id")
        ));
    }

    public Cart getCartById(int id) {
        String query = "SELECT * FROM carts WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> new Cart(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity"),
                rs.getInt("variant_id")
        ));
    }

}
