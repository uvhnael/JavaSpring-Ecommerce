package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    private int id;
    private String code;
    private String couponDescription;
    private double discountValue;
    private String discountType;
    private int timesUsed;
    private int maxUsage;
    private String couponStartDate;
    private String couponEndDate;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;

}

