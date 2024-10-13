package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.example.controller.PostController;
import org.example.repository.PostRepositoryImpl;
import org.example.service.PostService;

@Configuration
public class JavaConfig {
  @Bean
  // аргумент метода и есть DI
  // название метода - название бина
  public PostController postController(PostService service) {
    return new PostController(service);
  }

  @Bean
  public PostService postService(PostRepositoryImpl repository) {
    return new PostService(repository);
  }

  @Bean
  public PostRepositoryImpl postRepository() {
    return new PostRepositoryImpl();
  }
}
