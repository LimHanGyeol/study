package com.tommy.bootrestful.user.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.service.UserDaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminUserController {

    private final UserDaoService userDaoService;

    @GetMapping("/users")
    public MappingJacksonValue findAllUsers() {
        List<User> users = userDaoService.findAll();

        FilterProvider filters = createUserInfoFilters();
        return createMappingValue(users, filters);
    }

    @GetMapping("/users/{id}")
    public MappingJacksonValue findUserById(@PathVariable(name = "id") int userId) {
        User findUser = userDaoService.findById(userId);

        FilterProvider filters = createUserInfoFilters();
        return createMappingValue(findUser, filters);
    }

    private MappingJacksonValue createMappingValue(Object userData, FilterProvider filters) {
        MappingJacksonValue mappingValue = new MappingJacksonValue(userData);
        mappingValue.setFilters(filters);
        return mappingValue;
    }

    private FilterProvider createUserInfoFilters() {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinedDate", "ssn");

        return new SimpleFilterProvider().addFilter("UserInfo", filter);
    }
}
