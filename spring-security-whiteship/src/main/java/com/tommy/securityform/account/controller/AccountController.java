package com.tommy.securityform.account.controller;

import com.tommy.securityform.account.domain.Account;
import com.tommy.securityform.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 매우 간소화한 회원가입 핸들러이다. 이렇게 사용하지 말자.
     */
    @GetMapping("/account/{username}/{password}/{role}")
    public ResponseEntity<Account> createAccount(@ModelAttribute Account account) {
        Account savedAccount = accountService.createAccount(account);
        return ResponseEntity.ok().body(savedAccount);
    }
}
