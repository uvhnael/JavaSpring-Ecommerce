package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Model.Coupon;
import org.uvhnael.ecomapi.Repository.CouponRepository;
import org.uvhnael.ecomapi.exception.coupon.CouponNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public boolean createCoupon(Coupon coupon) {
        checkNull(coupon);
        return couponRepository.createCoupon(coupon.getCode(), coupon.getCouponDescription(), coupon.getDiscountValue(), coupon.getDiscountType(), coupon.getMaxUsage(), coupon.getCouponStartDate(), coupon.getCouponEndDate(), coupon.getCreatedBy());
    }

    public boolean updateCoupon(Coupon coupon) {
        checkNull(coupon);
        if (couponRepository.getCouponById(coupon.getId()) == null) {
            throw new CouponNotFoundException("Coupon ID: " + coupon.getId() + " not found");
        }
        return couponRepository.updateCoupon(coupon.getId(), coupon.getCode(), coupon.getCouponDescription(), coupon.getDiscountValue(), coupon.getDiscountType(), coupon.getMaxUsage(), coupon.getCouponStartDate(), coupon.getCouponEndDate(), coupon.getUpdatedBy());
    }

    public boolean deleteCoupon(int id) {
        if (couponRepository.getCouponById(id) == null) {
            throw new CouponNotFoundException("Coupon ID: " + id + " not found");
        }
        return couponRepository.deleteCoupon(id);
    }

    public List<Coupon> getByProductId(int productId) {
        return couponRepository.getByProductId(productId);
    }

    public List<List<Coupon>> getByProductIds(List<Integer> productIds) {
        List<List<Coupon>> coupons = new ArrayList<>();
        for (int productId : productIds) {
            coupons.add(couponRepository.getByProductId(productId));
        }
        return coupons;
    }

    public List<Coupon> getOrderCoupons() {
        return couponRepository.getOrderCoupons();
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.getAllCoupons();
    }

    private void checkNull(Coupon coupon) {
        if (coupon.getCode() == null) {
            throw new IllegalArgumentException("Coupon code cannot be null");
        }
        if (coupon.getCouponDescription() == null) {
            throw new IllegalArgumentException("Coupon description cannot be null");
        }
        if (coupon.getDiscountValue() == 0) {
            throw new IllegalArgumentException("Discount value cannot be 0");
        }
        if (coupon.getDiscountType() == null) {
            throw new IllegalArgumentException("Discount type cannot be null");
        }
        if (coupon.getMaxUsage() == 0) {
            throw new IllegalArgumentException("Max usage cannot be 0");
        }
        if (coupon.getCouponStartDate() == null) {
            throw new IllegalArgumentException("Coupon start date cannot be null");
        }
        if (coupon.getCouponEndDate() == null) {
            throw new IllegalArgumentException("Coupon end date cannot be null");
        }
        if (coupon.getCreatedBy() == null) {
            throw new IllegalArgumentException("Created by cannot be null");
        }
    }
}
