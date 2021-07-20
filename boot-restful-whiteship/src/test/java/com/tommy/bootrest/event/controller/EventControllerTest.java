package com.tommy.bootrest.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tommy.bootrest.common.RestDocsConfiguration;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import com.tommy.bootrest.event.domain.EventStatus;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO : 리팩토링 시 MockMvc UTF-8 설정 해주기
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

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
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event", // 문서의 이름
                        links(
                                halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing events"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(ACCEPT).description("accept header"),
                                headerWithName(CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders(
                                headerWithName(LOCATION).description("Location header"),
                                headerWithName(CONTENT_TYPE).description("content type header")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
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
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
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

    @Test
    @DisplayName("한건의 이벤트 조회")
    void getEvent() throws Exception {
        // given
        Event event = generateEvent(1);

        // when
        ResultActions response = mockMvc.perform(get("/api/events/{id}", event.getId()));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 조회 시 404 Error")
    void nonExistEvent() throws Exception {
        // when
        ResultActions response = mockMvc.perform(get("/api/events/1234"));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    void queryEvents() throws Exception {
        // given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // when
        ResultActions response = mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC"))
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventResourceList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event" + index)
                .description("test event")
                .build();

        return eventRepository.save(event);
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
 *
 * Rest Docs의 document에 들어오는 identifier는 문서의 이름이 된다.
 * description에는 좀 더 의미있는 내용을 적어주면 좋을 것 이다.
 *
 * mockMvc의 response 검증에서 rest docs의 경우 documents 에서 검증을 하고 있으므로,
 * jsonPath로 테스트하지 않아도 된다.
 */
