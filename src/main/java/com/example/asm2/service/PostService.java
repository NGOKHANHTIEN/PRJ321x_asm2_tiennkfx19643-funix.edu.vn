package com.example.asm2.service;

import com.example.asm2.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    List<Post> findAll();
    Post findById (int id);
    Post save(Post post);
    void deleteById (int id);

    List<Post> findByTitle(String title);

    List<Post> findByAddress(String address);
    List<Post> topPostApplied ();

    Page<Post> fetchPost(Pageable pageable);
}

