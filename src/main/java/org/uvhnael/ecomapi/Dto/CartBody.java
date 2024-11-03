package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartBody {
    private int id;
    private int customerId;
    private int productId;
    private int quantity;
    private int variantId;

}
