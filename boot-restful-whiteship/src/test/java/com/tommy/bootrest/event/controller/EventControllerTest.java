package com.tommy.bootrest.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventStatus;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO : 리팩토링 시 MockMvc UTF-8 설정 해주기
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("정상적으로 이벤트를 생성할 경우")
    void create_event() throws Exception {
        // given
        EventCreateRequest eventCreateRequest = newEventCreateRequestInstance();

        // when
        ResultActions response = mockMvc.perform(post("/api/events")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCreateRequest))
        ).andDo(print());

        // then
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(LOCATION))
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }

    @Test
    @DisplayName("이벤트 생성 시 허용하지 않은 값이 들어올 경우")
    void create_event_bad_request() throws Exception {
        // given
        Event event = newEventInstance();

        // when
        ResultActions response = mockMvc.perform(post("/api/events")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
        ).andDo(print());

        // then
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이벤트 생성 시 비어있는 값이 있을 경우")
    void create_event_bad_request_empty_input() throws Exception {
        // given
        EventCreateRequest eventCreateRequest = EventCreateRequest.builder().build();

        // when
        ResultActions response = mockMvc.perform(post("/api/events")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCreateRequest))
        ).andDo(print());

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());
    }

    @Test
    @DisplayName("이벤트 생성 시 잘못된 조건이 있을 경우")
    void create_event_bad_request_wrong_input() throws Exception {
        // given
        EventCreateRequest eventCreateRequest = EventCreateRequest.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 7, 9, 22, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 8, 22, 20))
                .beginEventDateTime(LocalDateTime.of(2020, 7, 7, 20, 0))
                .endEventDateTime(LocalDateTime.of(2020, 7, 6, 22, 0))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업 팩토리")
                .build();

        // when
        ResultActions response = mockMvc.perform(post("/api/events")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCreateRequest))
        ).andDo(print());

        // then
        response.andExpect(status().isBadRequest());
    }

    private EventCreateRequest newEventCreateRequestInstance() {
        return EventCreateRequest.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 7, 8, 22, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 9, 22, 20))
                .beginEventDateTime(LocalDateTime.of(2020, 7, 25, 20, 0))
                .endEventDateTime(LocalDateTime.of(2020, 7, 26, 22, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업 팩토리")
                .build();
    }

    private Event newEventInstance() {
        return Event.builder()
                .id(10L)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 7, 8, 22, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 9, 22, 20))
                .beginEventDateTime(LocalDateTime.of(2020, 7, 25, 20, 0))
                .endEventDateTime(LocalDateTime.of(2020, 7, 26, 22, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();
    }
}

/*
 * SpringBoot를 사용하면 ObjectMapper가 자동으로 Bean에 등록되어 있다.
 * 요청의 Input으로 들어오는 필드에서 정의하지 않은 값이 들어올 경우
 * SpringBoot가 제공하는 Properties를 이용해서 ObjectMapper 확장 기능을 이용하여 처리할 수 있다.
 * Input으로 들어오는 매개변수를 Deserialization 할때 unKnown한 변수가 있을 경우 실패하게 하라라는
 * 확장기능을 적용했다.
 * 올바르지 않은 필드가 추가될 경우 BadRequest를 보낼지, 그냥 DTO에서 무시하고 로직을 진행할지는 개인의 선택이다.
 */
