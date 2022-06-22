package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryRequest;
import com.devsuperior.dscatalog.dto.CategoryResponse;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
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
        return new CategoryResponse(byId.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
    }

    @Transactional
    public CategoryResponse insert(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category = categoryRepository.save(category);
        return new CategoryResponse(category);
    }

    @Transactional
    public CategoryResponse update(Long id,CategoryRequest categoryRequest) {
        try {
            var entity = categoryRepository.getOne(id);
            entity.setName(categoryRequest.getName());
            entity = categoryRepository.save(entity);
            return new CategoryResponse(entity);
        } catch (EntityNotFoundException e ) {
            throw  new ResourceNotFoundException("Id not found " + id);
        }
    }
}
