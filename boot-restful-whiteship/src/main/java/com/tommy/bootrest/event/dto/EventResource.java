package com.tommy.bootrest.event.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tommy.bootrest.event.controller.EventController;
import com.tommy.bootrest.event.domain.Event;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends RepresentationModel<EventResource> {

    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
        this.add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

    public Event getEvent() {
        return event;
    }
}
