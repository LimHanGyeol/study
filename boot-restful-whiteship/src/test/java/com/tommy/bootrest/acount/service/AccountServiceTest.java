package com.tommy.bootrest.acount.service;

import com.tommy.bootrest.acount.domain.Account;
import com.tommy.bootrest.acount.domain.AccountRepository;
import com.tommy.bootrest.acount.domain.AccountRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void loadUserByUsername() {
        // given
        String password = "hangyeol";
        String username = "hangyeol@email.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountRepository.save(account);

        // when
        UserDetails userDetails = accountService.loadUserByUsername(username);

        // then
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }
}
