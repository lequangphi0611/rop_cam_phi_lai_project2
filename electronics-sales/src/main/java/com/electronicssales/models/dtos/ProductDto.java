package com.electronicssales.models.dtos;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotBlank;

import com.electronicssales.entities.Paragraph;
import com.electronicssales.entities.ProductParameter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {

    long id;

    @NotBlank
    String productName;

    long price;

    Collection<Long> categoriesId = new ArrayList<>();

    long manufacturerId;

    Collection<Paragraph> paragraphs = new ArrayList<>();

    Collection<ProductParameter> productParameters = new ArrayList<>();

    Collection<Long> imageIds = new ArrayList<>();
    
}