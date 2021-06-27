package com.tommy.bootrestful.user.controller;

import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.domain.UserRepository;
import com.tommy.bootrestful.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RequiredArgsConstructor
@RequestMapping("/jpa")
@RestController
public class UserJpaController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> findUserById(@PathVariable long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[%d] not found", id)));

        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAllUsers());
        entityModel.add(linkTo.withRel("all-users"));
        return entityModel;
    }
}
