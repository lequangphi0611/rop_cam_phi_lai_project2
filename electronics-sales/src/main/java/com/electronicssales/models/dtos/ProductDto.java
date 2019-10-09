package com.electronicssales.models.dtos;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;

import com.electronicssales.entities.Paragraph;
import com.electronicssales.entities.ProductParameter;

import lombok.Data;

@Data
public class ProductDto {

    private long id;

    @NotBlank
    private String productName;

    private long price;

    Collection<Long> categoriesId = new ArrayList<>();

    private long manufacturerId;

    Collection<Paragraph> paragraphs = new ArrayList<>();

    Collection<ProductParameter> productParameters = new ArrayList<>();

    Collection<Long> imageIds = new ArrayList<>();
    
}