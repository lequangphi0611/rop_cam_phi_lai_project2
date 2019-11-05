package com.electronicssales.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Manufacturer;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.models.responses.ManufacturerResponse;

public interface ManufacturerService {

    Collection<ManufacturerResponse> findAll();

    ManufacturerResponse save(ManufacturerDto manufacturerDto);

    Collection<ManufacturerResponse> saveAll(Collection<ManufacturerDto> manufacturerDtos);

    boolean existsByManufacturerName(String manufacturerName);

    boolean existsById(long id);

    void deleteById(long id);

    Optional<Manufacturer> findByManufacturerName(String manufacturerName); 

    Optional<Manufacturer> findById(long id);

    List<ManufacturerResponse> findByCategoryId(long categoryId);
    
}