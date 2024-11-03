package org.uvhnael.ecomapi.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @CreatedDate
    @Indexed(expireAfter = "30d") // TTL for auto-deletion after 30 days
    private Date createdAt = new Date();
}
