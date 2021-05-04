package com.tommy.securityform.account.controller;

import com.tommy.securityform.WithUser;
import com.tommy.securityform.account.domain.Account;
import com.tommy.securityform.account.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * DB에 접근하는 테스트를 작성할 경우 영속성 컨텍스트의 1차 캐시로 인해,
 * createUser 의 작동이 불완전할 수 있다.
 * Test 이후 롤백할 의도로 Transaction을 설정하자.
 */
@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {

    private static final String USERNAME = "hangyeol";
    private static final String PASSWORD = "123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("익명으로 index 페이지 접근 - 코드 접근")
    void index_anonymousV1() throws Exception {
        mockMvc.perform(get("/").with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("익명으로 index 페이지 접근 - 어노테이션 접근")
    void index_anonymousV2() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인한 상태로 index 페이지 접근 - 코드 접근")
    void index_userV1() throws Exception {
        mockMvc.perform(get("/")
                .with(user("hangyeol").roles("USER"))) // 인증된 것을 가정하기 때문에 password 모킹은 의미 없을 수 있다.
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    @DisplayName("로그인한 상태로 index 페이지 접근 - 어노테이션 접근")
    void index_userV2() throws Exception {
        mockMvc.perform(get("/")) // 인증된 것을 가정하기 때문에 password 모킹은 의미 없을 수 있다.
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("admin 페이지에 USER 권한 접근")
    void admin_user() throws Exception {
        mockMvc.perform(get("/admin")
                .with(user("hangyeol").roles("USER"))) // 인증된 것을 가정하기 때문에 password 모킹은 의미 없을 수 있다.
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("admin 페이지에 ADMIN 권한 접근 - 코드 접근")
    void admin_adminV1() throws Exception {
        mockMvc.perform(get("/admin")
                .with(user("admin").roles("ADMIN"))) // 인증된 것을 가정하기 때문에 password 모킹은 의미 없을 수 있다.
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("admin 페이지에 ADMIN 권한 접근 - 어노테이션 접근")
    void admin_adminV2() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void login_success() throws Exception {
        Account user = createUser(USERNAME, PASSWORD);
        mockMvc.perform(formLogin().user(user.getUsername()).password(PASSWORD))
                .andExpect(authenticated());
    }

    @Test
    @Transactional
    void login_fail() throws Exception {
        Account user = createUser(USERNAME, PASSWORD);
        mockMvc.perform(formLogin().user(user.getUsername()).password("12345"))
                .andExpect(unauthenticated());
    }

    private Account createUser(String username, String password) {
        return accountService.createAccount(username, password, "USER");
    }
}
