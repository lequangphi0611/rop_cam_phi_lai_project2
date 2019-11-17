package com.electronicssales.services;

import java.util.List;

import com.electronicssales.entities.ParameterType;

public interface ParameterTypeService {

    ParameterType create(ParameterType parameterType);

    List<ParameterType> findAll();
    
}