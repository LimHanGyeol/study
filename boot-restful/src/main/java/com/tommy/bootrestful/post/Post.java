package com.tommy.bootrestful.post;

import com.tommy.bootrestful.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Post(Long id, String description, User user) {
        this.id = id;
        this.description = description;
        this.user = user;
    }
}
