package com.tommy.bootrestful.user.service;

import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.domain.UserRepository;
import com.tommy.bootrestful.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserJpaService {

    private final UserRepository userRepository;

    public User findByUserId(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[%d] not found", userId)));
    }
}
