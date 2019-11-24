package com.electronicssales.models.dtos;

import java.util.Collection;

import javax.validation.constraints.NotBlank;

import com.electronicssales.entities.Paragraph;

import org.springframework.web.multipart.MultipartFile;

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

    Collection<Long> categoryIds;

    Long manufacturerId;

    Collection<Paragraph> paragraphs;

    Collection<ProductParameterDto> productParameters;

    MultipartFile[] images;

    Integer quantity;
    
}