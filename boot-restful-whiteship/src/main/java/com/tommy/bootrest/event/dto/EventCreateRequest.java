package com.tommy.bootrest.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventCreateRequest {

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

    @Builder
    public EventCreateRequest(String name, String description, String location, int basePrice, int maxPrice, int limitOfEnrollment, LocalDateTime beginEnrollmentDateTime, LocalDateTime closeEnrollmentDateTime, LocalDateTime beginEventDateTime, LocalDateTime endEventDateTime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.basePrice = basePrice;
        this.maxPrice = maxPrice;
        this.limitOfEnrollment = limitOfEnrollment;
        this.beginEnrollmentDateTime = beginEnrollmentDateTime;
        this.closeEnrollmentDateTime = closeEnrollmentDateTime;
        this.beginEventDateTime = beginEventDateTime;
        this.endEventDateTime = endEventDateTime;
    }
}
