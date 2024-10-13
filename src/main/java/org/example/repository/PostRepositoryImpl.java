package org.example.repository;

import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final AtomicLong postId = new AtomicLong(1);
    Map<Long, Post> posts = new ConcurrentHashMap<Long, Post>();

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long id = postId.getAndIncrement();
            post.setId(id);
            posts.put(id, post);
            return post;
        }
        if (posts.containsKey(post.getId())) {
            posts.put(post.getId(), post);
            return post;
        }
        throw new NotFoundException("Post not found");
    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            posts.remove(id);
            return;
        }
        throw new NotFoundException("Post not found");
    }
}
