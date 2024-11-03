package org.uvhnael.ecomapi.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.uvhnael.ecomapi.Model.Event;

import java.util.Date;
import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByCustomerId(int customerId);

    List<Event> findByProductId(int productId);

    int countByProductId(int productId);

    List<Event> findAllByEventNameEquals(String eventName);

    List<Event> findByEventName(String eventName);

    List<Event> findByCreatedAt(Date createAt);

    List<Event> findByProductIdAndEventName(int productId, String eventName);

}
