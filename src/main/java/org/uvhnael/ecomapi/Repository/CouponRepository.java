package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.Coupon;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CouponRepository {

    private final JdbcTemplate jdbcTemplate;

    //    `id`, `code`, `coupon_description`, `discount_value`, `discount_type`, `times_used`, `max_usage`, `coupon_start_date`, `coupon_end_date`, `created_at`, `updated_at`, `created_by`, `updated_by`
    public boolean createCoupon(String code, String couponDescription, double discountValue, String discountType, int maxUsage, String couponStartDate, String couponEndDate, String createdBy) {
        String sql = "INSERT INTO coupons (code, coupon_description, discount_value, discount_type, max_usage, coupon_start_date, coupon_end_date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, code, couponDescription, discountValue, discountType, maxUsage, couponStartDate, couponEndDate, createdBy) == 1;
    }

    public boolean updateCoupon(int id, String code, String couponDescription, double discountValue, String discountType, int maxUsage, String couponStartDate, String couponEndDate, String updatedBy) {
        String sql = "UPDATE coupons SET code = ?, coupon_description = ?, discount_value = ?, discount_type = ?, max_usage = ?, coupon_start_date = ?, coupon_end_date = ?, updated_by = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        return jdbcTemplate.update(sql, code, couponDescription, discountValue, discountType, maxUsage, couponStartDate, couponEndDate, updatedBy, id) == 1;
    }

    public boolean deleteCoupon(int id) {
        String sql = "DELETE FROM coupons WHERE id = ?";
        return jdbcTemplate.update(sql, id) == 1;
    }

    public List<Coupon> getByProductId(int productId) {
        String sql = "SELECT c.* FROM coupons c JOIN product_coupons pc ON c.id = pc.coupon_id WHERE pc.product_id = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) -> new Coupon(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("coupon_description"),
                rs.getDouble("discount_value"),
                rs.getString("discount_type"),
                rs.getInt("times_used"),
                rs.getInt("max_usage"),
                rs.getString("coupon_start_date"),
                rs.getString("coupon_end_date"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public List<Coupon> getOrderCoupons() {
        String sql = "SELECT * FROM coupons WHERE discount_type = 'fixed' OR discount_type = 'percentage'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Coupon(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("coupon_description"),
                rs.getDouble("discount_value"),
                rs.getString("discount_type"),
                rs.getInt("times_used"),
                rs.getInt("max_usage"),
                rs.getString("coupon_start_date"),
                rs.getString("coupon_end_date"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }


    public void updateTimesUsed(int couponId) {
        String sql = "UPDATE coupons SET times_used = times_used + 1 WHERE id = ?";
        jdbcTemplate.update(sql, couponId);
    }

    public Coupon getCouponById(int id) {
        String sql = "SELECT * FROM coupons WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Coupon(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("coupon_description"),
                rs.getDouble("discount_value"),
                rs.getString("discount_type"),
                rs.getInt("times_used"),
                rs.getInt("max_usage"),
                rs.getString("coupon_start_date"),
                rs.getString("coupon_end_date"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public List<Coupon> getAllCoupons() {
        String sql = "SELECT * FROM coupons";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Coupon(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("coupon_description"),
                rs.getDouble("discount_value"),
                rs.getString("discount_type"),
                rs.getInt("times_used"),
                rs.getInt("max_usage"),
                rs.getString("coupon_start_date"),
                rs.getString("coupon_end_date"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }

    public Coupon isExist(int couponId) {
        String sql = "SELECT * FROM coupons WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{couponId}, (rs, rowNum) -> new Coupon(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("coupon_description"),
                rs.getDouble("discount_value"),
                rs.getString("discount_type"),
                rs.getInt("times_used"),
                rs.getInt("max_usage"),
                rs.getString("coupon_start_date"),
                rs.getString("coupon_end_date"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("created_by"),
                rs.getString("updated_by")
        ));
    }


}
