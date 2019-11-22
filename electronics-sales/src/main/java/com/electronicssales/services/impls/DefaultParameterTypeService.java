package com.electronicssales.services.impls;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.ParameterType;
import com.electronicssales.repositories.ParameterTypeRepository;
import com.electronicssales.services.ParameterTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Strings;

@Service
public class DefaultParameterTypeService implements ParameterTypeService {

    private static final int DEFAULT_PAGE_INDEX = 0;

    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private ParameterTypeRepository parameterTypeRepository;

    private ParameterType copyParameterType(ParameterType parameter) {
        return ParameterType.of(parameter.getId(), parameter.getParameterTypeName());
    }

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
            .map(this::copyParameterType)
            .collect(Collectors.toList());
    }

    @Override
    public List<ParameterType> findAll(String name) {
        if(!Strings.hasText(name)) {
            return this.findAll();
        }
        return parameterTypeRepository.findByParameterTypeNameContaining(name);
    }

    @Override
    public Optional<ParameterType> findByName(String name) {
        return parameterTypeRepository.findByParameterTypeName(name);
    }

    
}