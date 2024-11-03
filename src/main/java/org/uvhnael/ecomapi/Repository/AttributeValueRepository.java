package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.AttributeValue;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttributeValueRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<AttributeValue> getAttributeValues(int attributeId) {
        String sql = "SELECT * FROM attribute_values WHERE attribute_id = ?";
        return jdbcTemplate.query(sql, new Object[]{attributeId}, (rs, rowNum) -> new AttributeValue(
                rs.getInt("id"),
                rs.getInt("attribute_id"),
                rs.getString("attribute_value")
        ));
    }

    public List<String> getAttributeValuesByVariantID(int variantId) {
        String sql = "SELECT attribute_value FROM attribute_values WHERE id IN (SELECT attribute_value_id FROM variant_attribute_values WHERE variant_id = ?)";
        return jdbcTemplate.queryForList(sql, new Object[]{variantId}, String.class);

    }

    public boolean createAttributeValue(String attributeValue, int attributeId) {
        String sql = "INSERT INTO attribute_values (attribute_value, attribute_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, attributeValue, attributeId) > 0;
    }
}
