package com.eventease.backend.service;

import com.eventease.backend.dto.CategoryDTO;
import com.eventease.backend.entity.Category;
import com.eventease.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDTO createCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = new Category();
        category.setName(name);
        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    private CategoryDTO toDto(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
