package com.example.asm2.service;

import com.example.asm2.dao.CategoryRepository;
import com.example.asm2.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService{
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImp(CategoryRepository theCategoryRep){
        categoryRepository = theCategoryRep;
    }
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getCategoryByIds(List<Integer> id) {
        return categoryRepository.findAllById(id);
    }

    @Override
    public Category findById(int id){
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> topCategory() {
        List<Category> topCategory = getCategoryByIds(categoryRepository.topCategoryId());
        return  topCategory;
    }


}
