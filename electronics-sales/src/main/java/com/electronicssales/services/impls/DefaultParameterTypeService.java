package com.electronicssales.services.impls;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.ParameterType;
import com.electronicssales.repositories.ParameterTypeRepository;
import com.electronicssales.services.ParameterTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultParameterTypeService implements ParameterTypeService {

    @Autowired
    private ParameterTypeRepository parameterTypeRepository;

    @Override
    public ParameterType create(ParameterType parameterType) {
        return Optional.of(parameterType)
            .map(this.parameterTypeRepository::save)
            .get();
    }

    @Override
    public List<ParameterType> findAll() {
        return parameterTypeRepository.findAll()
            .stream()
            .map(parameter -> ParameterType.of(parameter.getId(), parameter.getParameterTypeName()))
            .collect(Collectors.toList());
    }

    
}