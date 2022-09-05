package com.devsuperior.dscatalog;

import com.devsuperior.dscatalog.dto.category.CategoryResponse;
import com.devsuperior.dscatalog.dto.product.ProductRequest;
import com.devsuperior.dscatalog.dto.product.ProductResponse;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Factory {

    public static Product createProdutc() {
        Set<Category> categories = new HashSet<>();
        categories.add(createCategory());
        Product product = new Product(1L, "Phone", "Goof Phone", 800.0, "https://image",
                Instant.parse("2022-10-20T03:00:00Z"), categories);
        return product;
    }

    public static ProductResponse createProductResponse() {
        Product product = createProdutc();
        ProductResponse response = new ProductResponse(product, product.getCategories());
        return response;
    }

    public static Category createCategory() {
        return new Category(4L, "Eletronico", Instant.parse("2022-10-20T03:00:00Z"), Instant.parse("2022-10-20T03:00:00Z"));
    }

    public static ProductRequest createProdutcRequest() {
        List<CategoryResponse> categories = new ArrayList<>();
        CategoryResponse categoryResponse = new CategoryResponse(10L, "Teste");
        categories.add(categoryResponse);
        ProductRequest productRequest = new ProductRequest("Phone", "Goof Phone", 800.0, "https://image",
                Instant.parse("2022-10-20T03:00:00Z"), categories);
        return productRequest;
    }
}
