package com.tommy.bootrestful.user.controller;

import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.service.UserDaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserDaoService userDaoService;

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable(name = "id") long userId) {
        return userDaoService.findById(userId);
    }

    @PostMapping("/users")
    public void createUser(@RequestBody User user) {
        User savedUser = userDaoService.save(user);
    }
}
