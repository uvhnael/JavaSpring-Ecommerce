package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "productrates")
public class ProductRate {

    @Id
    private String id;

    private Long productId;
    private String productAttributeValue;
    private Long customerId;
    private String customerName;
    private Long orderItemId;
    private Double rate;
    private String review;
    private String videoPath;
    private List<String> imagePath;
    private Date createdAt = new Date();

}
