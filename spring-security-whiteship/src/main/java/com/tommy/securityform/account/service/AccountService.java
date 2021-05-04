package com.tommy.securityform.account.service;

import com.tommy.securityform.account.domain.Account;
import com.tommy.securityform.account.domain.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsService 는 DAO 로 유저 정보를 가져와 인증하는데 사용한다.
 * 하는 일은 유저이름을 받아와서 유저이름에 해당하는 유저 정보 데이터를 가져와서
 * UserDetails 타입으로 리턴한다.
 */
@Service
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account createAccount(String role, String username, String password) {
        Account account = new Account(role, username, password);
        account.encodePassword();
        return accountRepository.save(account);
    }
}
