package com.electronicssales.models.responses;

import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CategoryResponse extends BaseCategoryResponse {

    private int productCount;

    Collection<CategoryResponse> childrens;

}