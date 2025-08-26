package com.example.asm2.service;

import com.example.asm2.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    List<Category> getCategoryByIds(List<Integer> ids);
    Category findById(int id);
    List<Category> topCategory();
}
