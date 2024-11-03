package org.uvhnael.ecomapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uvhnael.ecomapi.Model.Variant;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBody {
    private String productName;
    private String description;
    private double regularPrice;
    private int quantity;
    private boolean isPublished;
    private int createdBy;
    private List<String> attributes;
    private List<List<String>> attributeValues;
    private List<Variant> variants;
    private List<String> imagePath;
    private int categoryId;

}
