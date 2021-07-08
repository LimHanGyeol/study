package com.tommy.bootrest.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventStatus;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import org.hamcrest.Matchers;
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

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
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
