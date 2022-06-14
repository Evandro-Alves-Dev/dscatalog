package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse implements Serializable {

    private Long id;
    private String name;

    public CategoryResponse(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }
}
