package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uvhnael.ecomapi.Model.Address;
import org.uvhnael.ecomapi.Model.Order;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private int id;
    private double price;
    private Address address;
    private int couponId;
    private int customerId;
    private int customerAddressId;
    private int orderStatusId;
    private String orderApproveAt;
    private String orderDeliveredCarrierDate;
    private String orderDeliveredCustomerDate;
    private String createdAt;
    private List<OrderItemResponse> orderItems;

    public static OrderResponse from(Order order, Address address, List<OrderItemResponse> orderItems) {
        return new OrderResponse(
                order.getId(),
                order.getPrice(),
                address,
                order.getCouponId(),
                order.getCustomerId(),
                order.getCustomerAddressId(),
                order.getOrderStatusId(),
                order.getOrderApproveAt(),
                order.getOrderDeliveredCarrierDate(),
                order.getOrderDeliveredCustomerDate(),
                order.getCreatedAt(),
                orderItems
        );
    }

}
