package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.dto.product.ProductRequest;
import com.devsuperior.dscatalog.dto.product.ProductResponse;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.exceptions.DataBaseException;
import com.devsuperior.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existId;
    private Long nonExistId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductRequest productRequest;

    @BeforeEach
    void setup() throws Exception {
        existId = 1L;
        nonExistId = 1000L;
        dependentId = 4L;
        product = Factory.createProdutc();
        category = Factory.createCategory();
        productRequest = Factory.createProdutcRequest();
        page = new PageImpl<>(List.of(product));

        when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        when(productRepository.findById(existId)).thenReturn(Optional.of(product));
        when(productRepository.findById(nonExistId)).thenReturn(Optional.empty());

        when(productRepository.getOne(existId)).thenReturn(product);
        when(productRepository.getOne(nonExistId)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getOne(existId)).thenReturn(category);
        when(categoryRepository.getOne(nonExistId)).thenThrow(EntityNotFoundException.class);

        doNothing().when(productRepository).deleteById(existId);
        doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistId);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void saveShouldReturnProductWhenValidEntity() {
       ProductResponse response =  productService.insert(productRequest);

        Assertions.assertNotNull(response);
    }

    @Test
    public void updateShouldReturnProductWhenExistsId() {
        ProductResponse response = productService.update(existId, productRequest);

        Assertions.assertNotNull(response);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenDoesNotExistId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(nonExistId, productRequest);
        });
        verify(productRepository, times(1)).getOne(nonExistId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenDoesNotExistsId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(nonExistId);
        });
        verify(productRepository, times(1)).findById(nonExistId);
    }

    @Test
    public void findByIdShouldReturnProductWhenExistsId() {
        Long id = 1L;
        ProductResponse response = productService.findById(existId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(id, response.getId());
        verify(productRepository, times(1)).findById(existId);
    }

    @Test
    public void findAllPagedShouldReturnedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponse> response = productService.findAllPaged(pageable);

        Assertions.assertNotNull(response);
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existId);
        });
        verify(productRepository, times(1)).deleteById(existId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistId);
        });
        verify(productRepository, times(1)).deleteById(nonExistId);
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.delete(dependentId);
        });
        verify(productRepository, times(1)).deleteById(dependentId);
    }
}