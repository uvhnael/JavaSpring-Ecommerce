package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Model.Event;
import org.uvhnael.ecomapi.Repository.EventRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void addEvent(int customerId, int productId, String eventName) {
        eventRepository.save(new Event(customerId, productId, eventName, new Date()));
    }

    public List<Event> getEventsByCustomerId(int customerId) {
        return eventRepository.findByCustomerId(customerId);
    }

}
