package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommend {
    private int id;
    private double price;
    private double rating;
    private int salesVolume;
    private int category;
    private int userInterest;


}
