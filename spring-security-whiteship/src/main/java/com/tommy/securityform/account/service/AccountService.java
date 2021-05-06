package com.tommy.securityform.account.service;

import com.tommy.securityform.account.domain.Account;
import com.tommy.securityform.account.domain.AccountRepository;
import com.tommy.securityform.account.domain.UserAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserAccount(account);
    }

    public Account createAccount(Account account) {
        account.encodePassword(passwordEncoder);
        return accountRepository.save(account);
    }
}
