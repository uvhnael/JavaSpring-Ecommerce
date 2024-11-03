package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id;
    private double price;
    private int couponId;
    private int customerId;
    private int customerAddressId;
    private int orderStatusId;
    private String orderApproveAt;
    private String orderDeliveredCarrierDate;
    private String orderDeliveredCustomerDate;
    private String createdAt;
    private List<OrderItem> orderItems;

    public Order(int id, double price, int couponId, int customerId, int customerAddressId, int orderStatusId, String orderApproveAt, String orderDeliveredCarrierDate, String orderDeliveredCustomerDate, String createdAt) {
        this.id = id;
        this.price = price;
        this.couponId = couponId;
        this.customerId = customerId;
        this.customerAddressId = customerAddressId;
        this.orderStatusId = orderStatusId;
        this.orderApproveAt = orderApproveAt;
        this.orderDeliveredCarrierDate = orderDeliveredCarrierDate;
        this.orderDeliveredCustomerDate = orderDeliveredCustomerDate;
        this.createdAt = createdAt;
    }

}
