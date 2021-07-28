package com.tommy.bootrest.event.service;

import com.tommy.bootrest.acount.domain.Account;
import com.tommy.bootrest.acount.exception.NotMatchAccountException;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.EventResponse;
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

    public Page<EventResponse> findEventResponseAll(Pageable pageable) {
        Page<Event> events = findAll(pageable);
        return events.map(EventResponse::of);
    }

    private Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public EventResponse findEventResponseById(Long id) {
        Event findEvent = findEventById(id);
        return EventResponse.of(findEvent, findEvent.getManagerId());
    }

    private Event findEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNonExistException("Event non Exist id : " + id));
    }

    public boolean validateEventManager(Long id, Account account) {
        Event findEvent = findEventById(id);
        return findEvent.isCreatedByManager(account);
    }

    @Transactional
    public EventResponse saveEvent(EventCreateRequest eventCreateRequest, Account account) {
        Event event = modelMapper.map(eventCreateRequest, Event.class);
        event.update();
        event.updateLocation();
        event.createdEventByManager(account);

        Event savedEvent = eventRepository.save(event);
        return EventResponse.of(savedEvent, account.getId());
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventUpdateRequest eventUpdateRequest, Account account) {
        Event event = findEventById(id);
        if (!event.isCreatedByManager(account)) {
            throw new NotMatchAccountException();
        }
        event.updateEvent(eventUpdateRequest.getName(), eventUpdateRequest.getDescription());
        return EventResponse.of(event);
    }
}
