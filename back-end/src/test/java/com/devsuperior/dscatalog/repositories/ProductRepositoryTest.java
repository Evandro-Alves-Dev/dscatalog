package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long existingId2;
    private Long existingIdNull;
    private Long countTotalProducts;

    @BeforeEach
    void setup () throws Exception {
        existingId = 1L;
        existingId2 = 100L;
        existingIdNull = null;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);
        Optional<Product> result = repository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExistis() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(existingId2);
        });
    }

    @Test
    public void deleteShouldThrowInvalidDataAccessApiUsageExceptionWhenIdDoesNotExistis() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            repository.deleteById(existingIdNull);
        });
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = Factory.createProdutc();
        product.setId(null);
        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnNotNullWhenIdExists() {
        Optional<Product> product = repository.findById(existingId);

        Assertions.assertTrue(product.isPresent());
    }

    @Test
    public void findByIdShouldReturnNullWhenIdDoesNotExists() {
        Optional<Product> product = repository.findById(existingId2);

        Assertions.assertTrue(product.isEmpty());
    }

    @Test
    public void findByIdShouldThrowInvalidDataAccessApiUsageExceptionWhenIdDoesNotExistis() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            repository.findById(existingIdNull);
        });
    }

}