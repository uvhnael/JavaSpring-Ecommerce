package org.uvhnael.ecomapi.Model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private int id;
    private String name;
    private String dob;
    private String imagePath;
    private String email;
    private String password;
    private String phoneNumber;
    private String active;
    private String registeredAt;
}
