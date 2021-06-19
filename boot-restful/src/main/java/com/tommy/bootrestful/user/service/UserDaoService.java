package com.tommy.bootrestful.user.service;

import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1L, "Tommy", LocalDateTime.now()));
        users.add(new User(2L, "Hangyeol", LocalDateTime.now()));
        users.add(new User(3L, "Louie", LocalDateTime.now()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            int userCount = users.size();
            user.calculateUserId(++userCount);
        }
        if (user.getJoinedDate() == null) {
            user.updateJoinedDate(LocalDateTime.now());
        }
        users.add(user);
        return user;
    }

    public User findById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        throw new UserNotFoundException(String.format("ID[%d] not found", id));
    }
}
