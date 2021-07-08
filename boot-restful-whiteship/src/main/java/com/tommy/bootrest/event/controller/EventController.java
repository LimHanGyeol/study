package com.tommy.bootrest.event.controller;

import com.tommy.bootrest.event.domain.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.MediaType.*;

@RequestMapping(value = "/api/events", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RestController
public class EventController {

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {

        URI location = linkTo(this.getClass())
                .slash("{id}")
                .toUri();
        event.setId(10L);
        return ResponseEntity.created(location).body(event);
    }
}
