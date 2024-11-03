package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Model.Attribute;
import org.uvhnael.ecomapi.Repository.AttributeRepository;
import org.uvhnael.ecomapi.Repository.AttributeValueRepository;
import org.uvhnael.ecomapi.exception.attribute.AttributeCreationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;

    public List<Attribute> getByProductId(int productId) {
        List<Attribute> attributes = attributeRepository.getByProductId(productId);
        attributes.forEach(attribute -> attribute.setAttributeValues(attributeValueRepository.getAttributeValues(attribute.getId())));
        return attributes;
    }

    public void createAttributes(List<String> attributes, List<List<String>> attributeValues, int productId, int createdBy) {
        for (String attribute : attributes) {
            int attributeId = attributeRepository.createAttribute(attribute, productId, createdBy);
            if (attributeId == 0) {
                throw new AttributeCreationException("Attribute creation failed");
            }
            for (String attributeValue : attributeValues.get(attributes.indexOf(attribute))) {
                if (!attributeValueRepository.createAttributeValue(attributeValue, attributeId)) {
                    throw new AttributeCreationException("Attribute value creation failed");
                }
            }
        }
    }

}
