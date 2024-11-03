package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.Gallery;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GalleryRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Gallery> getByProductId(int productId) {
        String sql = "SELECT * FROM galleries WHERE product_id = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> new Gallery(
                rs.getInt("id"),
                rs.getInt("product_id"),
                rs.getString("image_path"),
                rs.getBoolean("thumbnail"),
                rs.getInt("display_order"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public String getThumbnail(int productId) {
        String sql = "SELECT image_path FROM galleries WHERE product_id = ? AND thumbnail = true";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, String.class);
    }

    public boolean createGallery(int productId, String imagePath, boolean thumbnail, int displayOrder, int createdBy) {
        String sql = "INSERT INTO galleries (product_id, image_path, thumbnail, display_order, created_by) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, productId, imagePath, thumbnail, displayOrder, createdBy) > 0;
    }
}
