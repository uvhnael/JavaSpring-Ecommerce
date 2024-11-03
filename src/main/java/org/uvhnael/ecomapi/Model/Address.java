package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private int id;
    private int customerId;
    private boolean isDefault;
    private String customerName;
    private String addressLine1;
    private String addressLine2;
    private String phoneNumber;
    private String city;
    private String district;
    private String ward;

}
