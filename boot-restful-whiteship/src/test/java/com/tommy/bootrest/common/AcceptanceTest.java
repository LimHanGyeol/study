package com.tommy.bootrest.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tommy.bootrest.acount.application.AccountService;
import com.tommy.bootrest.acount.domain.Account;
import com.tommy.bootrest.acount.domain.AccountRepository;
import com.tommy.bootrest.acount.domain.AccountRole;
import com.tommy.bootrest.common.config.AppProperties;
import com.tommy.bootrest.event.domain.Event;
import com.tommy.bootrest.event.domain.EventRepository;
import com.tommy.bootrest.event.domain.EventStatus;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Import(RestDocsConfiguration.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
public class AcceptanceTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AppProperties appProperties;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    protected void setUp() throws Exception {
        eventRepository.deleteAll();
        accountRepository.deleteAll();
    }

    protected String getBearerAccessToken(boolean needToCreateAccount) throws Exception {
        // given
        if (needToCreateAccount) { // 회원가입 // 현재 테스트 2개 실패함, 이유는 계정 index를 통일할 필요가 있ㅇ르 듯
            newAccount(1000L);
        }

        // when
        ResultActions response = 로그인_요청();

        // then
        String responseBody = response.andReturn()
                .getResponse()
                .getContentAsString();

        Map map = objectMapper.readValue(responseBody, Map.class);
        return "Bearer" + map.get("access_token").toString();
    }

    private ResultActions 로그인_요청() throws Exception {
        return mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", appProperties.getUserUsername() + 1000L)
                .param("password", appProperties.getUserPassword())
                .param("grant_type", "password")
                .accept(MediaType.APPLICATION_JSON));
    }

    private Account newAccount(long index) {
        String username = appProperties.getUserUsername() + index;
        Account account = Account.builder()
                .email(username)
                .password(appProperties.getUserPassword())
                .roles(Set.of(AccountRole.USER))
                .build();
        return accountService.saveAccount(account);
    }

    protected Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event" + index)
                .description("test event")
                .build();
        event.createdEventByManager(newAccount(index));
        return eventRepository.save(event);
    }

    protected Event newEventOfLoginAccount(long index) {
        Event event = buildEvent();
        event.createdEventByManager(newAccount(index));
        return eventRepository.save(event);
    }

    private Event buildEvent() {
        return Event.builder()
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
