package com.electronicssales.services;

import java.util.Collection;
import java.util.Optional;

import com.electronicssales.entities.Manufacturer;
import com.electronicssales.models.dtos.ManufacturerDto;

public interface ManufacturerService {

    Collection<ManufacturerDto> findAll();

    Manufacturer save(ManufacturerDto manufacturerDto);

    Collection<Manufacturer> saveAll(Collection<ManufacturerDto> manufacturerDtos);

    boolean existsByManufacturerName(String manufacturerName);

    boolean existsById(long id);

    void deleteById(long id);

    Optional<Manufacturer> findByManufacturerName(String manufacturerName); 

    Optional<Manufacturer> findById(long id);

    Collection<ManufacturerDto> findByCategoryId(long categoryId);
    
}