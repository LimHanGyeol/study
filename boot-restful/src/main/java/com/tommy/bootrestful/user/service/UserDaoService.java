package com.tommy.bootrestful.user.service;

import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1L, "Tommy", LocalDateTime.now(), "pass1", "701010-1111111"));
        users.add(new User(2L, "Hangyeol", LocalDateTime.now(), "pass2", "801010-2222222"));
        users.add(new User(3L, "Louie", LocalDateTime.now(), "pass3", "901010-1111111"));
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

    public User findById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        throw new UserNotFoundException(String.format("ID[%d] not found", id));
    }

    public void deleteById(int id) {
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.getId() == id) {
                iterator.remove();
                return;
            }
        }
        throw new UserNotFoundException(String.format("ID[%d] not found", id));
    }
}
