package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SellRepository {

    private final JdbcTemplate jdbcTemplate;

    public void addSell(int productId, int quantity, double price) {
        String sql = "INSERT INTO sells (product_id, quantity, price) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, productId, quantity, price);
    }

    public int getSalesVolume(int productId) {
        String sql = "SELECT SUM(quantity) FROM sells WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class) == null ? 0 : jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class);
    }


}
