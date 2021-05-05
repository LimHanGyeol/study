package com.tommy.securityform;

import com.tommy.securityform.account.domain.Account;
import com.tommy.securityform.account.utils.AccountContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class SampleService {

    public void dashboard() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();// userDetailsService
        System.out.println("============");
        System.out.println(userDetails.getUsername());
    }
}
