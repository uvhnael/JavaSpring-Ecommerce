package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Dto.NamePrice;
import org.uvhnael.ecomapi.Model.Product;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Integer> getProducts(int page, int size) {
        String sql = "SELECT * FROM products LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, page * size}, (rs, rowNum) -> rs.getInt("id"));
    }

    public List<Integer> getRandomProducts(int size) {
        String sql = "SELECT * FROM products ORDER BY RAND() LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{size}, (rs, rowNum) -> rs.getInt("id"));
    }

    public List<Integer> getRandomProductsByCategory(int categoryId, int size) {
        String sql = "SELECT * FROM products WHERE id IN (SELECT product_id FROM product_categories WHERE category_id = ?) ORDER BY RAND() LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{categoryId, size}, (rs, rowNum) -> rs.getInt("id"));
    }

    public List<Integer> searchProducts(String keyword) {
        String sql = "SELECT * FROM products WHERE product_name LIKE ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%"}, (rs, rowNum) -> rs.getInt("id"));
    }

    public Product getById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("product_name"),
                rs.getDouble("regular_price"),
                rs.getDouble("discount_price"),
                rs.getInt("quantity"),
                rs.getDouble("rate"),
                rs.getString("description"),
                rs.getBoolean("is_published"),
                rs.getBoolean("is_deleted"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public List<Product> getProductResponse(List<Integer> ids) {
        // Dynamically construct the placeholders for the IN clause
        String placeholders = String.join(",", ids.stream().map(id -> "?").toArray(String[]::new));
        String sql = "SELECT * FROM products WHERE id IN (" + placeholders + ") AND is_published = 1 AND is_deleted = 0";

        // Convert List<Integer> to Object[] for query arguments
        Object[] params = ids.toArray();

        // Execute query and map results
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> new Product(
                rs.getInt("id"),
                rs.getString("product_name"),
                rs.getDouble("regular_price"),
                rs.getInt("quantity"),
                rs.getDouble("rate")
        ));
    }

    public List<Integer> getByCategoryId(int categoryId) {
        String sql = "SELECT * FROM products WHERE id IN (SELECT product_id FROM product_categories WHERE category_id = ?)";
        return jdbcTemplate.query(sql, new Object[]{categoryId}, (rs, rowNum) -> rs.getInt("id"));
    }

    public void updateQuantity(int productId, int quantity) {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, productId);
    }

    public String getProductName(int productId) {
        String sql = "SELECT product_name FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, String.class);
    }

    public int createProduct(String productName, double regularPrice, int quantity, String description, boolean isPublished, int createdBy) {
        String sql = "INSERT INTO products (product_name, regular_price, quantity, description, is_published, created_by) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, productName, regularPrice, quantity, description, isPublished, createdBy);
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

    }

}
