package com.electronicssales.services;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.ParameterType;

public interface ParameterTypeService {

    ParameterType create(ParameterType parameterType);

    Optional<ParameterType> findByName(String name);

    List<ParameterType> findAll();

    List<ParameterType> findAll(String name);
    
}