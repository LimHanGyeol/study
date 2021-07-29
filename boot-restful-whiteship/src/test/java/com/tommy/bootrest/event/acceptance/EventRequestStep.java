package com.tommy.bootrest.event.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.EventUpdateRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class EventRequestStep {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public EventRequestStep(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public ResultActions 이벤트_생성_요청(EventCreateRequest eventCreateRequest, String accessToken) throws Exception {
        return mockMvc.perform(post("/api/events")
                .header(AUTHORIZATION, accessToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCreateRequest))
        );
    }

    public ResultActions 이벤트_한_건_조회_요청(long id) throws Exception {
        return mockMvc.perform(get("/api/events/{id}", id));
    }

    public ResultActions 로그인_전_이벤트_목록_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC"));
    }

    public ResultActions 로그인_후_이벤트_목록_조회_요청(String accessToken) throws Exception {
        ResultActions response = mockMvc.perform(get("/api/events")
                .header(AUTHORIZATION, accessToken)
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC"));
        return response;
    }

    public ResultActions 로그인_및_이벤트_수정_요청(long eventId, EventUpdateRequest eventUpdateRequest, String accessToken) throws Exception {
        return mockMvc.perform(put("/api/events/{id}", eventId)
                .header(AUTHORIZATION, accessToken)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventUpdateRequest)));
    }

    public EventCreateRequest newEventCreateRequest(int basePrice) {
        return EventCreateRequest.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 7, 8, 22, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 9, 22, 20))
                .beginEventDateTime(LocalDateTime.of(2020, 7, 25, 20, 0))
                .endEventDateTime(LocalDateTime.of(2020, 7, 26, 22, 0))
                .basePrice(basePrice)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타트업 팩토리")
                .build();
    }
}
