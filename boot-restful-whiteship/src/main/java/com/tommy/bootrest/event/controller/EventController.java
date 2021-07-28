package com.tommy.bootrest.event.controller;

import com.tommy.bootrest.acount.domain.Account;
import com.tommy.bootrest.acount.domain.CurrentUser;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.EventResponse;
import com.tommy.bootrest.event.dto.EventUpdateRequest;
import com.tommy.bootrest.event.dto.EventValidator;
import com.tommy.bootrest.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = APPLICATION_JSON_VALUE)
@RestController
public class EventController {

    private final EventService eventService;
    private final PagedResourcesAssembler<EventResponse> assembler;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> createEvent(@RequestBody @Valid EventCreateRequest eventCreateRequest,
                                                     @CurrentUser Account account) {
        EventResponse savedEventResponse = eventService.saveEvent(eventCreateRequest, account);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(this.getClass()).slash(savedEventResponse.getId());
        savedEventResponse.add(linkTo(EventController.class).withRel("query-events"));
        savedEventResponse.add(selfLinkBuilder.withRel("update-event"));
        savedEventResponse.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));

        URI location = selfLinkBuilder.toUri();
        return ResponseEntity.created(location).body(savedEventResponse);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<EventResponse>>> queryEvent(Pageable pageable,
                                                                             @CurrentUser Account account) {
        Page<EventResponse> eventResponses = eventService.findEventResponseAll(pageable);

        PagedModel<EntityModel<EventResponse>> eventResources = assembler.toModel(eventResponses);
        eventResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        if (account != null) {
            eventResources.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(eventResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long id,
                                                  @CurrentUser Account account) {
        EventResponse eventResponse = eventService.findEventResponseById(id);
        eventResponse.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));

        if (eventService.validateEventManager(id, account)) {
            eventResponse.add(linkTo(EventController.class).slash(eventResponse.getId()).withRel("update-event"));
        }

        return ResponseEntity.ok(eventResponse);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id,
                                                     @Valid @RequestBody EventUpdateRequest eventUpdateRequest,
                                                     @CurrentUser Account account) {
        EventResponse eventResponse = eventService.updateEvent(id, eventUpdateRequest, account);

        // DTO 검증 Validation. EventCreateValid와 같은 방식으로 처리하면 된다.
        eventResponse.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));
        return ResponseEntity.ok(eventResponse);
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
