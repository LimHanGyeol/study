package com.tommy.bootrest.event.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String location;

    private int basePrice; // optional

    private int maxPrice; // optional

    private int limitOfEnrollment;

    private boolean offline;

    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    private LocalDateTime beginEnrollmentDateTime;

    private LocalDateTime closeEnrollmentDateTime;

    private LocalDateTime beginEventDateTime;

    private LocalDateTime endEventDateTime;

    @Builder
    public Event(Long id, String name, String description, String location, int basePrice, int maxPrice, int limitOfEnrollment, boolean offline, boolean free, EventStatus eventStatus, LocalDateTime beginEnrollmentDateTime, LocalDateTime closeEnrollmentDateTime, LocalDateTime beginEventDateTime, LocalDateTime endEventDateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.basePrice = basePrice;
        this.maxPrice = maxPrice;
        this.limitOfEnrollment = limitOfEnrollment;
        this.offline = offline;
        this.free = free;
        this.eventStatus = eventStatus;
        this.beginEnrollmentDateTime = beginEnrollmentDateTime;
        this.closeEnrollmentDateTime = closeEnrollmentDateTime;
        this.beginEventDateTime = beginEventDateTime;
        this.endEventDateTime = endEventDateTime;
    }

    public void update() {
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
            return;
        }
        this.free = false;
    }

    public void updateLocation() {
        if (this.location == null || this.location.isBlank()) {
            this.offline = false;
            return;
        }
        this.offline = true;
    }

    public void updateEvent(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return getId().equals(event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

/*
 * Equals, HashCode를 사용할 때 모든 필드를 사용하는 경우가 있다.
 * 하지만 이럴 경우 Entity 연관관계 매핑을 했을 때 각 객체가 상호참조 될 경우
 * EqualsAndHashCode를 구현한 코드 내에서 스택오버플로우가 생길 수 있다.
 * 그래서 주로 id와 같은 식별자 역할을 하는 값만 Equals, HashCode 설정을 한다.
 */
