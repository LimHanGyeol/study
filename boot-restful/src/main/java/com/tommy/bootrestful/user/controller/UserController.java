package com.tommy.bootrestful.user.controller;

import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.service.UserDaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
    public User findUserById(@PathVariable(name = "id") int userId) {
        return userDaoService.findById(userId);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") int userId) {
        userDaoService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
