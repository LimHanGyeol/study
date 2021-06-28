package com.tommy.bootrestful.post.controller;

import com.tommy.bootrestful.post.domain.Post;
import com.tommy.bootrestful.post.domain.PostRepository;
import com.tommy.bootrestful.user.domain.User;
import com.tommy.bootrestful.user.service.UserJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class PostJpaController {

    private final PostRepository postRepository;
    private final UserJpaService userJpaService;

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable(name = "id") long userId,
                                           @RequestBody Post post) {
        User user = userJpaService.findByUserId(userId);
        post.registerAuthor(user);
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
