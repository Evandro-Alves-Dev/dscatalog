package com.devsuperior.dscatalog.dto.product;

import com.devsuperior.dscatalog.dto.category.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private Instant date;
    private List<CategoryResponse> categories = new ArrayList<>();
}
