package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.Attribute;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttributeRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Attribute> getAll() {
        String sql = "SELECT * FROM attributes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Attribute(
                rs.getInt("id"),
                rs.getString("attribute_name"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public Attribute getById(int id) {
        String sql = "SELECT * FROM attributes WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Attribute(
                rs.getInt("id"),
                rs.getString("attribute_name"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public List<Attribute> getByProductId(int productId) {
        String sql = "SELECT * FROM attributes WHERE id IN (SELECT attribute_id FROM product_attributes WHERE product_id = ?)";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> new Attribute(
                rs.getInt("id"),
                rs.getString("attribute_name"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public int createAttribute(String attributeName, int productId, int createdBy) {
        String sql = "INSERT INTO attributes (attribute_name, created_by) VALUES (?, ?)";
        jdbcTemplate.update(sql, attributeName, createdBy);
        int attributeId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        String sql2 = "INSERT INTO product_attributes (product_id, attribute_id) VALUES (?, ?)";
        jdbcTemplate.update(sql2, productId, attributeId);
        return attributeId;
    }


    public boolean isAttributeExistsByProductId(int productId) {
        String sql = "SELECT COUNT(*) FROM attributes WHERE id IN (SELECT attribute_id FROM product_attributes WHERE product_id = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class) > 0;
    }
}
