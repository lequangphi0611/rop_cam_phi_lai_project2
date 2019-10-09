package com.electronicssales.repositories;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.ParameterType;

public interface CustomizeCategoryRepository {

    void saveCategoryParameter(Category category, ParameterType parameterType);
    
}