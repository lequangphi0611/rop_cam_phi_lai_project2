package com.electronicssales.models.dtos;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;

import com.electronicssales.entities.ParameterType;

import lombok.Data;

@Data
public class CategoryDto {

    private long id;

    @NotBlank
    private String categoryName;

    private Collection<ParameterType> parameterTypes = new ArrayList<>();

    public CategoryDto() {
        super();
    }

    public CategoryDto(long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
    
}