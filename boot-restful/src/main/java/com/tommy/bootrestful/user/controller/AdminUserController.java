package com.tommy.bootrestful.user.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.domain.UserV2;
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

    @GetMapping("/v1/users")
    public MappingJacksonValue findAllUsersV1() {
        List<User> users = userDaoService.findAll();

        FilterProvider filters = createUserInfoFilters("ssn", "UserInfo");
        return createMappingValue(users, filters);
    }

    @GetMapping("/v1/users/{id}")
    public MappingJacksonValue findUserByIdV1(@PathVariable(name = "id") int userId) {
        User findUser = userDaoService.findById(userId);

        FilterProvider filters = createUserInfoFilters("ssn", "UserInfo");
        return createMappingValue(findUser, filters);
    }

    @GetMapping("/v2/users/{id}")
    public MappingJacksonValue findUserByIdV2(@PathVariable(name = "id") int userId) {
        User findUser = userDaoService.findById(userId);

        UserV2 userV2 = new UserV2(findUser, "VIP");

        FilterProvider filters = createUserInfoFilters("grade", "UserInfoV2");
        return createMappingValue(userV2, filters);
    }

    /**
     * Request Param을 이용한 Version 관리
     */
    @GetMapping(value = "/users/{id}", params = "version=3")
    public MappingJacksonValue findUserByIdV3(@PathVariable(name = "id") int userId) {
        User findUser = userDaoService.findById(userId);

        FilterProvider filters = createUserInfoFilters("ssn", "UserInfo");
        return createMappingValue(findUser, filters);
    }

    /**
     * HTTP Header를 이용한 Version 관리
     */
    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=4")
    public MappingJacksonValue findUserByIdV4(@PathVariable(name = "id") int userId) {
        User findUser = userDaoService.findById(userId);

        FilterProvider filters = createUserInfoFilters("ssn", "UserInfo");
        return createMappingValue(findUser, filters);
    }

    private FilterProvider createUserInfoFilters(String expect, String filterName) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinedDate", expect);

        return new SimpleFilterProvider().addFilter(filterName, filter);
    }

    private MappingJacksonValue createMappingValue(Object userData, FilterProvider filters) {
        MappingJacksonValue mappingValue = new MappingJacksonValue(userData);
        mappingValue.setFilters(filters);
        return mappingValue;
    }
}
