package com.tommy.bootrest.event.controller;

import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.EventValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.MediaType.*;

@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@RestController
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@Valid @RequestBody EventCreateRequest eventCreateRequest,
                                             Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        eventValidator.validate(eventCreateRequest, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventCreateRequest, Event.class);
        event.update();
        event.updateLocation();

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
 *
 * spring-starter-validation을 이용하여 RequestBody의 Validation을 수행할 수 있다.
 * Validation이 완료되면 해당 결과가 Errors 객체에 담긴다.
 * Errors 내부의 값을 확인하여 error가 존재할 경우 BadRequest를 보내는 식의 처리를 할 수 있다.
 */
