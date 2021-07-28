package com.tommy.bootrest.event.dto;

import com.tommy.bootrest.event.dto.validator.annotation.EventCreateValid;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * EventCreateValid 라는 Custom Validation을 사용함으로써
 * 기존의 Annotation 기반 Valid는 사용하지 않게 됐다.
 * 더 많은 처리 방법을 생각하여 상황별로 좋은 처리 방법을 생각하자.
 */
@Getter
@EventCreateValid
@NoArgsConstructor
public class EventCreateRequest {

    private String name;
    private String description;
    private String location; // optional (장소 정보가 없으면 온라인 모임)

    private int basePrice; // optional
    private int maxPrice; // optional

    private int limitOfEnrollment;

    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
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
