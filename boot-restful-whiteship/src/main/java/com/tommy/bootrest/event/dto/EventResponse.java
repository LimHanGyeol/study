package com.tommy.bootrest.event.dto;

import com.tommy.bootrest.event.presentation.EventController;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
@NoArgsConstructor
public class EventResponse extends RepresentationModel<EventResponse> {

    private Long id;
    private Long managerId;

    private String name;
    private String description;
    private String location; // 장소 정보가 없으면 온라인 모임

    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;

    private boolean offline;
    private boolean free;

    private EventStatus eventStatus;

    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;



    private EventResponse(Event event) {
        this.id = event.getId();
        this.managerId = event.getManagerId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.offline = event.isOffline();
        this.free = event.isFree();
        this.eventStatus = event.getEventStatus();
        this.basePrice = event.getBasePrice();
        this.maxPrice = event.getMaxPrice();
        this.limitOfEnrollment = event.getLimitOfEnrollment();
        this.beginEnrollmentDateTime = event.getBeginEnrollmentDateTime();
        this.closeEnrollmentDateTime = event.getCloseEnrollmentDateTime();
        this.beginEventDateTime = event.getBeginEventDateTime();
        this.endEventDateTime = event.getEndEventDateTime();
        this.add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

    private EventResponse(Event event, Long managerId) {
        this.id = event.getId();
        this.managerId = managerId;
        this.name = event.getName();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.offline = event.isOffline();
        this.free = event.isFree();
        this.eventStatus = event.getEventStatus();
        this.basePrice = event.getBasePrice();
        this.maxPrice = event.getMaxPrice();
        this.limitOfEnrollment = event.getLimitOfEnrollment();
        this.beginEnrollmentDateTime = event.getBeginEnrollmentDateTime();
        this.closeEnrollmentDateTime = event.getCloseEnrollmentDateTime();
        this.beginEventDateTime = event.getBeginEventDateTime();
        this.endEventDateTime = event.getEndEventDateTime();
        this.add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

    public static EventResponse of(Event event) {
        return new EventResponse(event);
    }

    public static EventResponse of(Event event, Long managerId) {
        return new EventResponse(event, managerId);
    }
}
