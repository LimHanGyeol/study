package com.tommy.securityform.account.controller;

import com.tommy.securityform.WithUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}
