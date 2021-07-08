package com.tommy.bootrest.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventRepository eventRepository;

    @Test
    void create_event() throws Exception {
        // given
        Event event = newEventInstance();
        given(eventRepository.save(event)).willReturn(event);

        // when
        ResultActions result = mockMvc.perform(post("/api/events")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
        ).andDo(print());

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(LOCATION))
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE));
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
                .build();
    }
}

/*
 * SpringBoot를 사용하면 ObjectMapper가 자동으로 Bean에 등록되어 있다.
 */
