package com.electronicssales.models.dtos;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ProductDto {

    private long id;

    @NotBlank
    private String productName;

    private long price;

    Collection<CategoryDto> categories;

    private long manufacturerId;

    Collection<ParagraphDto> paragraphDtos = new ArrayList<>();

    Collection<ProductParameterDto> productParameterDtos = new ArrayList<>();

    Collection<Long> imageIds = new ArrayList<>();
    
}