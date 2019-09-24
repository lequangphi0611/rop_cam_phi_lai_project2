package com.electronicssales.models.dtos;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;

import com.electronicssales.entities.ParameterType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CategoryDto {

    private long id;

    @NotBlank
    private String categoryName;

    @JsonIgnore
    private Collection<ParameterType> parameterTypes = new ArrayList<>();

    public CategoryDto() {
    }

    public CategoryDto(long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
    
}