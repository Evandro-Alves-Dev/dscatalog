package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryResponse;
import com.devsuperior.dscatalog.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        var list = categoryRepository.findAll();
        return list
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        var byId = categoryRepository.findById(id);
        return new CategoryResponse(byId.orElseThrow(() -> new EntityNotFoundException("Entity not found")));
    }
}
