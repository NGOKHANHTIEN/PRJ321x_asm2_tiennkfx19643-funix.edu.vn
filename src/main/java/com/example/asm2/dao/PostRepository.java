package com.example.asm2.dao;

import com.example.asm2.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post> findByTitleContainingIgnoreCase(String title);

    List<Post> findByAddressContainingIgnoreCase(String address);

    @Query(value = "SELECT post_id, COUNT(user_id) AS apply_count " +
            "FROM applications " +
            "GROUP BY post_id " +
            "ORDER BY apply_count DESC " +
            "LIMIT 4", nativeQuery = true)
    List<Integer> topAppliedPost ();
    Page<Post> findAll(Pageable pageable);

}
