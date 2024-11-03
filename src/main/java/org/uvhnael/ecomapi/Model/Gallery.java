package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gallery {
    private int id;
    private int productId;
    private String imagePath;
    private Boolean thumbnail;
    private int displayOrder;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}
