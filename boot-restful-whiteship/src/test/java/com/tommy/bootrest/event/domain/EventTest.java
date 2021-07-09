package com.tommy.bootrest.event.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @DisplayName("이벤트가 무료 입장 / 유료 입장1 / 유료 입장2 / 경매일 경우")
    @ParameterizedTest
    @MethodSource
    void provideAccessCondition(int basePrice, int maxPrice, boolean expected) {
        // given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isEqualTo(expected);
    }

    private static Stream<Arguments> provideAccessCondition() {
        return Stream.of(
                Arguments.of(0, 0, true),
                Arguments.of(100, 0, false),
                Arguments.of(100, 200, false),
                Arguments.of(0, 100, false)
        );
    }

    @DisplayName("장소가 온라인, 오프라인으로 나뉘어질 경우")
    @ParameterizedTest
    @CsvSource(value = {"'',false", "강남역 네이버 D2 스타트업 팩토리, true"}, delimiter = ',')
    void locationStatus(String location, boolean expected) {
        // given
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API Development with Spring")
                .location(location)
                .build();

        // when
        event.updateLocation();

        // then
        assertThat(event.isOffline()).isEqualTo(expected);
    }
}
/*
 * 테스트 케이스의 중복을 제거하는 수단으로 ParameterizedTest를 사용할 수 있다.
 * CsvSource, ValueSource, NullAndEmptySource, MethodSource 등을 사용할 수 있고,
 * 주로 CsvSource 혹은 ValueSource를 사용했는데, MethodSource도 염두를 해본다.
 * MethodSource를 사용할 경우 인자로 사용할 메서드와 테스트 메서드의 이름을 같게 하면
 * 어노테이션에 값을 주지 않아도 자동으로 인자로 사용할 메서드를 찾아 사용한다.
 */
