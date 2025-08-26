package com.example.asm2.dao;

import com.example.asm2.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    @Query(value="select category_id " +
            "from post_categories " +

            "group by category_id order by count(post_id) desc limit 4 ", nativeQuery = true )
    List<Integer> topCategoryId();


}
