package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.Variant;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VariantRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Variant> getByProductId(int productId) {
        String sql = "SELECT * FROM variants WHERE product_id = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> new Variant(
                rs.getInt("id"),
                rs.getInt("product_id"),
                rs.getDouble("price"),
                rs.getInt("quantity")
        ));
    }

    public Variant getById(int id) {
        String sql = "SELECT * FROM variants WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Variant(
                rs.getInt("id"),
                rs.getInt("product_id"),
                rs.getDouble("price"),
                rs.getInt("quantity")
        ));
    }

    public void updateQuantity(int id, int quantity) {
        String sql = "UPDATE variants SET quantity = quantity - ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, id);
    }

    public boolean createVariant(int productId, double price, int quantity) {
        String sql = "INSERT INTO variants (product_id, price, quantity) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, productId, price, quantity) > 0;
    }
}
