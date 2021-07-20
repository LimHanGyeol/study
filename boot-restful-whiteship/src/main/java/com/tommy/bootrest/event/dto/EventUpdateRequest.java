package com.tommy.bootrest.event.dto;

import com.tommy.bootrest.event.domain.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventUpdateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private String location; // optional (장소 정보가 없으면 온라인 모임)

    @Min(0)
    private int basePrice; // optional

    @Min(0)
    private int maxPrice; // optional

    @Min(0)
    private int limitOfEnrollment;

    @NotNull
    private LocalDateTime beginEnrollmentDateTime;

    @NotNull
    private LocalDateTime closeEnrollmentDateTime;

    @NotNull
    private LocalDateTime beginEventDateTime;

    @NotNull
    private LocalDateTime endEventDateTime;

    public EventUpdateRequest(Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.basePrice = event.getBasePrice();
        this.maxPrice = event.getMaxPrice();
        this.limitOfEnrollment = event.getLimitOfEnrollment();
        this.beginEnrollmentDateTime = event.getBeginEnrollmentDateTime();
        this.closeEnrollmentDateTime = event.getCloseEnrollmentDateTime();
        this.beginEventDateTime = event.getBeginEventDateTime();
        this.endEventDateTime = event.getEndEventDateTime();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void updateMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }
}
