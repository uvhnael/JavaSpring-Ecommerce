package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
    private int id;
    private String attributeName;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<AttributeValue> attributeValues;

    public Attribute(int id, String attributeName, String createdAt, String updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.attributeName = attributeName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
