package com.tommy.bootrest.event.controller;

import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.MediaType.*;

@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RestController
public class EventController {

    private final EventRepository eventRepository;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventRepository.save(event);

        URI location = linkTo(this.getClass())
                .slash(savedEvent.getId())
                .toUri();
        return ResponseEntity.created(location).body(event);
    }
}
