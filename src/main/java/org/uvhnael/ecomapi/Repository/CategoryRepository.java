package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.uvhnael.ecomapi.Model.Category;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Category> getAll() {
        String sql = "SELECT * FROM categories";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Category(
                rs.getInt("id"),
                rs.getInt("parent_id"),
                rs.getString("category_name"),
                rs.getString("category_description"),
                rs.getString("icon"),
                rs.getBoolean("active"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public Category getById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Category(
                rs.getInt("id"),
                rs.getInt("parent_id"),
                rs.getString("category_name"),
                rs.getString("category_description"),
                rs.getString("icon"),
                rs.getBoolean("active"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ), id);
    }

    public Category getByProductId(int productId) {
        String sql = "SELECT * FROM categories WHERE id IN (SELECT category_id FROM product_categories WHERE product_id = ?)";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Category(
                rs.getInt("id"),
                rs.getInt("parent_id"),
                rs.getString("category_name"),
                rs.getString("category_description"),
                rs.getString("icon"),
                rs.getBoolean("active"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ), productId);
    }

    public int getIdByProductID(int productId) {
        String sql = "SELECT category_id FROM product_categories WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, productId);
    }

    public boolean isCategoryExists(int id) {
        String sql = "SELECT COUNT(*) FROM categories WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


    public boolean addCategory(Category category) {
        String sql = "INSERT INTO categories (parent_id, category_name, category_description, icon, active, created_at, updated_at, created_by, updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, category.getParentId(), category.getCategoryName(), category.getCategoryDescription(), category.getIcon(), category.getActive(), category.getCreatedAt(), category.getUpdatedAt(), category.getCreatedBy(), category.getUpdatedBy());
        return rowsAffected > 0;
    }

    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET parent_id = ?, category_name = ?, category_description = ?, icon = ?, active = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, category.getParentId(), category.getCategoryName(), category.getCategoryDescription(), category.getIcon(), category.getActive(), category.getUpdatedAt(), category.getUpdatedBy(), category.getId());
        return rowsAffected > 0;
    }

    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public void addProductCategory(int productId, int categoryId) {
        String sql = "INSERT INTO product_categories (product_id, category_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, productId, categoryId);
    }

}
