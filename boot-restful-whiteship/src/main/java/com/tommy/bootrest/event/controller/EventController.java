package com.tommy.bootrest.event.controller;

import com.tommy.bootrest.acount.domain.Account;
import com.tommy.bootrest.acount.domain.CurrentUser;
import com.tommy.bootrest.common.errors.ErrorResource;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.EventResource;
import com.tommy.bootrest.event.dto.EventUpdateRequest;
import com.tommy.bootrest.event.dto.EventValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = APPLICATION_JSON_VALUE)
@RestController
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createEvent(@Valid @RequestBody EventCreateRequest eventCreateRequest,
                                      Errors errors,
                                      @CurrentUser Account account) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorResource(errors));
        }
        eventValidator.validate(eventCreateRequest, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorResource(errors));
        }

        Event event = modelMapper.map(eventCreateRequest, Event.class);
        event.update();
        event.updateLocation();
        event.createdEventByManager(account);

        Event savedEvent = eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(this.getClass()).slash(savedEvent.getId());
        URI location = selfLinkBuilder.toUri();

        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(location).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvent(Pageable pageable,
                                     PagedResourcesAssembler<Event> assembler,
                                     @CurrentUser Account account) {
        Page<Event> findAll = eventRepository.findAll(pageable);
        PagedModel<EventResource> eventResources = assembler.toModel(findAll, EventResource::new);
        eventResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        if (account != null) {
            eventResources.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(eventResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Long id,
                                   @CurrentUser Account account) {
        // controllerAdvice, exceptionHandler로 ResourceCustomException 만들어 404 처리하기
        Event event = eventRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));

        if (event.getManager().equals(account)) {
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }

        return ResponseEntity.ok(eventResource);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateEvent(@PathVariable Long id,
                                      @Valid @RequestBody EventUpdateRequest eventUpdateRequest,
                                      Errors errors,
                                      @CurrentUser Account account) {
        // controllerAdvice, exceptionHandler로 ResourceCustomException 만들어 404 처리하기 notFound
        Event savedEvent = eventRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (!savedEvent.getManager().equals(account)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        // DTO 검증 Validation. 제네릭 염두 하기
        // eventValidator.validate(eventUpdateRequest, errors);
        // if (errors.hasErrors()) {
        // return ResponseEntity.badRequest().build();
        // }

        savedEvent.updateEvent(eventUpdateRequest.getName(), eventUpdateRequest.getDescription());
        // service layer와 transactional 을 적용하지 않아 update를 수동으로 해준다.
        Event updatedEvent = eventRepository.save(savedEvent);

        EventResource eventResource = new EventResource(updatedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));
        return ResponseEntity.ok(eventResource);
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
 *
 * badRequest하는 부분을 controllerAdvice로 변경을 고려
 */
