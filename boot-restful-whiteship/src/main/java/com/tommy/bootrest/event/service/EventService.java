package com.tommy.bootrest.event.service;

import com.tommy.bootrest.acount.domain.Account;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.EventUpdateRequest;
import com.tommy.bootrest.event.exception.EventNonExistException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Event findEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNonExistException("Event non Exist id : " + id));
    }

    @Transactional
    public Event saveEvent(EventCreateRequest eventCreateRequest, Account account) {
        Event event = modelMapper.map(eventCreateRequest, Event.class);
        event.update();
        event.updateLocation();
        event.createdEventByManager(account);

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, EventUpdateRequest eventUpdateRequest) {
        Event event = findEventById(id);
        event.updateEvent(eventUpdateRequest.getName(), eventUpdateRequest.getDescription());
        return event;
    }
}
