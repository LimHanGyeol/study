package com.tommy.bootrest.event.acceptance;

import com.tommy.bootrest.common.AcceptanceTest;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventStatus;
import com.tommy.bootrest.event.dto.EventCreateRequest;
import com.tommy.bootrest.event.dto.EventUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc는 내부적으로 MockHttpServletRequest와 MockHttpServletResponse를 사용한다.
 * MockHttpServletResponse는 내부적인 charset이 "ISO-8859-1"로 설정되어 있다.
 * 그래서 mockMvc 테스트 응답의 경우 한글이 깨지는 경우가 있다.
 * 이를 UTF-8로 변경하기 위해 application.properties에
 * server.servlet.encoding.charset=utf-8
 * server.servlet.encoding.force-response=true
 * 두 설정을 추가했다.
 * 이럴 경우 응답 값을 검증할때 content-type:application/json;charset=utf-8의 경우로 값이 나올 수 있다.
 * Spring 5.2와 Spring Boot 2.2.0에서 content-type은 단일 값만 갖게 변경됐다. 이로인해 테스트가 실패할 수 있다.
 * 이 부분을 염두해서 개발을 해야 한다.
 * 현재는 properties에 정의했지만, 이는 CharacterEncodingFilter를 Bean으로 재정의하여 UTF-8을 지정하고,
 * SecurityFilter에 CsrfFilter.class 앞에 정의하여 해결할 수도 있다.
 *
 * <p>
 * 참조
 * http://honeymon.io/tech/2019/10/23/spring-deprecated-media-type.html
 * https://namocom.tistory.com/832
 * https://keichee.tistory.com/457
 * https://stackoverflow.com/questions/20863489/characterencodingfilter-dont-work-together-with-spring-security-3-2-0/23051264#23051264
 */
class EventControllerTest extends AcceptanceTest {

    private EventRequestStep requestStep;
    private String accessToken;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        requestStep = new EventRequestStep(mockMvc, objectMapper);
        accessToken = getBearerAccessToken(true);
    }

    @Test
    @DisplayName("정상적으로 이벤트를 생성할 경우")
    void create_event() throws Exception {
        // given
        EventCreateRequest eventCreateRequest = requestStep.newEventCreateRequest(100);

        // when
        ResultActions response = requestStep.이벤트_생성_요청(eventCreateRequest, accessToken);

        // then
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(LOCATION))
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
    @DisplayName("이벤트 생성 시 잘못된 값이 들어올 경우")
    void create_event_bad_request() throws Exception {
        // given
        EventCreateRequest eventCreateRequest = requestStep.newEventCreateRequest(-1);

        // when
        ResultActions response = requestStep.이벤트_생성_요청(eventCreateRequest, accessToken);

        // then
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이벤트 생성 시 비어있는 값이 있을 경우")
    void create_event_bad_request_empty_input() throws Exception {
        // given
        EventCreateRequest eventCreateRequest = EventCreateRequest.builder().build();

        // when
        ResultActions response = requestStep.이벤트_생성_요청(eventCreateRequest, accessToken);

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    @DisplayName("이벤트 생성 시 잘못된 조건이 있을 경우")
    void create_event_bad_request_wrong_input() throws Exception {
        // given
        EventCreateRequest eventCreateRequest = requestStep.newEventCreateRequest(10000);

        // when
        ResultActions response = requestStep.이벤트_생성_요청(eventCreateRequest, accessToken);

        // then
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("한건의 이벤트 조회")
    void getEvent() throws Exception {
        // given
        Event savedEvent = newEventOfLoginAccount(1234);

        // when
        ResultActions response = requestStep.이벤트_한_건_조회_요청(savedEvent.getId());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 조회 시 404 NotFound Error")
    void nonExistEvent() throws Exception {
        // when
        ResultActions response = requestStep.이벤트_한_건_조회_요청(1234L);

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 전 30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    void queryEvents() throws Exception {
        // given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // when
        ResultActions response = requestStep.로그인_전_이벤트_목록_조회_요청();

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventResponseList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));
    }


    @Test
    @DisplayName("로그인 후 30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    void queryEventsWithAuthentication() throws Exception {
        // given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // when
        ResultActions response = requestStep.로그인_후_이벤트_목록_조회_요청(accessToken);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventResponseList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.create-event").exists())
                .andDo(document("query-events"));
    }

    @Disabled
    @Test
    @DisplayName("이벤트를 정상적으로 수정하기")
    void updateEvent() throws Exception {
        // given
        String updatedEventName = "updated event";
        String updatedEventDescription = "updated description";
        Event event = newEventOfLoginAccount(1000);
        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest(event);
        eventUpdateRequest.updateName(updatedEventName);
        eventUpdateRequest.updateDescription(updatedEventDescription);

        // when
        ResultActions response = requestStep.로그인_및_이벤트_수정_요청(event.getId(), eventUpdateRequest, accessToken);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("name").value(updatedEventName))
                .andExpect(jsonPath("description").value(updatedEventDescription))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));
    }

    @Test
    @DisplayName("입력값이 비어있는 경우에 이벤트 수정 실패")
    void invalidUpdateEventV1() throws Exception {
        // given
        Event savedEvent = newEventOfLoginAccount(1L);
        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest();

        // when
        ResultActions response = requestStep.로그인_및_이벤트_수정_요청(savedEvent.getId(), eventUpdateRequest, accessToken);

        // then
        response.andExpect(status().isBadRequest());
    }

    @Disabled
    @Test
    @DisplayName("입력값이 잘못된 경우에 이벤트 수정 실패")
    void invalidUpdateEventV2() throws Exception {
        // given
        Event savedEvent = newEventOfLoginAccount(1L);
        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest(savedEvent);
        eventUpdateRequest.updateBasePrice(20000);
        eventUpdateRequest.updateMaxPrice(1000);

        // when
        ResultActions response = requestStep.로그인_및_이벤트_수정_요청(savedEvent.getId(), eventUpdateRequest, accessToken);

        // then
        response.andExpect(status().isBadRequest());
    }

    @Disabled
    @Test
    @DisplayName("존재하지 않는 이벤트 수정 실패")
    void invalidUpdateEventV3() throws Exception {
        // given
        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest();

        // when
        ResultActions response = requestStep.로그인_및_이벤트_수정_요청(200L, eventUpdateRequest, accessToken);

        // then
        response.andExpect(status().isNotFound());
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
