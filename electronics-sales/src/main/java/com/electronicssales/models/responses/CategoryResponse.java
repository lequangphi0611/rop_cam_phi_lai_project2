package com.electronicssales.models.responses;

import com.electronicssales.models.dtos.CategoryDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CategoryResponse extends CategoryDto {

    private int productCount;

    public CategoryResponse() {
        super();
    }

    public CategoryResponse(long id, String name, int productCount) {
        super(id, name);
        this.productCount = productCount;
    }

    
}