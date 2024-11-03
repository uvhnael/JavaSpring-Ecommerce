package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private int id;
    private int customerId;
    private int productId;
    private int quantity;
    private int variantId;
}
