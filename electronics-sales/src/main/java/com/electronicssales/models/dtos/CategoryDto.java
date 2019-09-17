package com.electronicssales.models.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CategoryDto {

    private long id;

    @NotBlank
    private String categoryName;

    public CategoryDto() {
        super();
    }

    public CategoryDto(long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
    
}