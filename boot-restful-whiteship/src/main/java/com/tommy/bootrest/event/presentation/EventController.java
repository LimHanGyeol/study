package com.tommy.bootrest.event.presentation;

import com.tommy.bootrest.acount.domain.Account;
import com.tommy.bootrest.acount.domain.CurrentUser;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.EventResponse;
import com.tommy.bootrest.event.dto.EventUpdateRequest;
import com.tommy.bootrest.event.application.EventService;
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

        // DTO ?????? Validation. EventCreateValid??? ?????? ???????????? ???????????? ??????.
        eventResponse.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));
        return ResponseEntity.ok(eventResponse);
    }
}

/*
 * ModelMapper??? ???????????? DTO ?????? ???????????? DTO -> Domain ???????????? ??? ??? ??????.
 * ??????????????? Reflection??? ???????????? ????????? ????????? ?????? ??? ??????.
 * ?????? Java??? Reflection ????????? ???????????? ????????? ??????????????? ?????? ????????? ???????????? ?????? ????????????.
 *
 * spring-starter-validation??? ???????????? RequestBody??? Validation??? ????????? ??? ??????.
 * Validation??? ???????????? ?????? ????????? Errors ????????? ?????????.
 * Errors ????????? ?????? ???????????? error??? ????????? ?????? BadRequest??? ????????? ?????? ????????? ??? ??? ??????.
 *
 * badRequest?????? ????????? controllerAdvice??? ????????? ??????
 */
