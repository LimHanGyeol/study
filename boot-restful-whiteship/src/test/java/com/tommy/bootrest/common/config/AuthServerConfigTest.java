package com.tommy.bootrest.common.config;

import com.tommy.bootrest.acount.domain.Account;
import com.tommy.bootrest.acount.domain.AccountRole;
import com.tommy.bootrest.acount.service.AccountService;
import com.tommy.bootrest.common.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthServerConfigTest extends AcceptanceTest {

    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("인증 토큰을 발급받는 테스트")
    void getAuthToken() throws Exception {
        // given
        String clientId = "myApp";
        String clientSecret = "pass";
        String username = "hangyeol@email.com";
        String password = "hangyeol";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountService.saveAccount(account);

        // when
        ResultActions response = mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password")
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("token_type").value("bearer"))
                .andExpect(jsonPath("refresh_token").isNotEmpty())
                .andExpect(jsonPath("expires_in").isNumber())
                .andExpect(jsonPath("scope").value("read write"));
    }
}
