package com.tommy.bootrest.event.controller;

import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventCreateRequest eventCreateRequest) {
        Event event = modelMapper.map(eventCreateRequest, Event.class);
        Event savedEvent = eventRepository.save(event);

        URI location = linkTo(this.getClass())
                .slash(savedEvent.getId())
                .toUri();
        return ResponseEntity.created(location).body(event);
    }
}

/*
 * ModelMapper를 이용하여 DTO 변환 코드없이 DTO -> Domain 형변환을 할 수 있다.
 * 내부적으로 Reflection이 사용되기 때문에 속도가 느릴 수 있다.
 * 점점 Java의 Reflection 성능도 좋아지고 있지만 거슬린다면 변환 로직을 구현하는 것도 방법이다.
 */
