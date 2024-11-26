package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "events")
public class Event {
    private int customerId;
    private int productId;
    private String eventName;
    private Date createdAt = new Date();
}
