package com.tommy.bootrest.event.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void builder() {
        // when
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API Development with Spring")
                .build();

        // then
        assertThat(event).isNotNull();
    }

    @DisplayName("이벤트가 무료 입장일 경우")
    @Test
    void free() {
        // given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isTrue();
    }

    @DisplayName("이벤트가 유료 입장일 경우")
    @Test
    void charge() {
        // given
        Event event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isFalse();
    }

    @DisplayName("이벤트의 입장권이 경매 형태일 입장일 경우")
    @Test
    void charge_auction() {
        // given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    void offline() {
        // given
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타트업 팩토리")
                .build();

        // when
        event.updateLocation();

        // then
        assertThat(event.isOffline()).isTrue();
    }

    @Test
    void online() {
        // given
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API Development with Spring")
                .build();

        // when
        event.updateLocation();

        // then
        assertThat(event.isOffline()).isFalse();
    }
}
