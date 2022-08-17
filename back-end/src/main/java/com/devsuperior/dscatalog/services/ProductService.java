package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.category.CategoryRequest;
import com.devsuperior.dscatalog.dto.category.CategoryResponse;
import com.devsuperior.dscatalog.dto.product.ProductRequest;
import com.devsuperior.dscatalog.dto.product.ProductResponse;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.exceptions.DataBaseException;
import com.devsuperior.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllPaged(Pageable pageable) {
        var list = productRepository.findAll(pageable);
        return list
                .map(product -> new ProductResponse(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        var byId = productRepository.findById(id);
        var productResponse = byId.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductResponse(productResponse, productResponse.getCategories());
    }

    @Transactional
    public ProductResponse insert(ProductRequest productRequest) {
        Product product = new Product();
        copyDtoToEntity(productRequest, product);
        product = productRepository.save(product);
        return new ProductResponse(product);
    }

    @Transactional
    public ProductResponse update(Long id,ProductRequest productRequest) {
        try {
            var entity = productRepository.getOne(id);
            copyDtoToEntity(productRequest, entity);
            entity = productRepository.save(entity);
            return new ProductResponse(entity);
        } catch (EntityNotFoundException e ) {
            throw  new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductRequest productRequest, Product product) {
        product.setName(productRequest.getName());
        product.setDate(productRequest.getDate());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setImgUrl(productRequest.getImgUrl());

        product.getCategories().clear();
        for (CategoryResponse catResponse : productRequest.getCategories()) {
            Category category = categoryRepository.getOne(catResponse.getId());
            product.getCategories().add(category);
        }
    }
}
