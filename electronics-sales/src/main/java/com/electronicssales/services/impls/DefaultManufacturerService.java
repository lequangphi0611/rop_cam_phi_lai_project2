package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.Image;
import com.electronicssales.entities.Manufacturer;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.repositories.ManufacturerRepository;
import com.electronicssales.services.ManufacturerService;
import com.electronicssales.utils.TwoDimensionalMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DefaultManufacturerService implements ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private TwoDimensionalMapper<ManufacturerDto, Manufacturer> manufacturerMapper;

    @Override
    public Collection<ManufacturerDto> findAll() {
        return manufacturerRepository
            .findAll()
            .stream()
            .map(manufacturerMapper::mapping)
            .collect(Collectors.toList());
    }

    @Override
    public Manufacturer save(ManufacturerDto manufacturerDto) {
        return manufacturerRepository
            .save(manufacturerMapper.secondMapping(manufacturerDto));
    }

    @Override
    public Collection<Manufacturer> saveAll(Collection<ManufacturerDto> manufacturerDtos) {
        return manufacturerRepository
            .saveAll(
                manufacturerDtos
                    .stream()
                    .map(manufacturerMapper::secondMapping)
                    .collect(Collectors.toList())
            );
    }

    @Override
    public boolean existsByManufacturerName(String manufacturerName) {
        return manufacturerRepository.existsByManufacturerName(manufacturerName);
    }

    @Override
    public Optional<Manufacturer> findByManufacturerName(String manufacturerName) {
        return manufacturerRepository.findByManufacturerName(manufacturerName);
    }

    @Override
    public Optional<Manufacturer> findById(long id) {
        return manufacturerRepository.findById(id);
    }

    @Override
    public Collection<ManufacturerDto> findByCategoryId(long categoryId) {
        return manufacturerRepository
            .findByCategoryId(categoryId)
            .stream()
            .map(manufacturerMapper::mapping)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(long id) {
        return existsById(id);
    }

    @Override
    public void deleteById(long id) {
        manufacturerRepository.deleteById(id);
    }

    @Component
    class ManufacturerMapper implements TwoDimensionalMapper<ManufacturerDto, Manufacturer> {

        @Override
        public ManufacturerDto mapping(Manufacturer manufacturer) {
            ManufacturerDto manufacturerDto = new ManufacturerDto();
            manufacturerDto.setId(manufacturer.getId());
            manufacturerDto.setLogoId(manufacturer.getManufacturerLogo().getId());
            manufacturerDto.setManufacturerName(manufacturer.getManufacturerName());
            return manufacturerDto;
        }

        @Override
        public Manufacturer secondMapping(ManufacturerDto manufacturerDto) {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setId(manufacturerDto.getId());
            if(manufacturerDto.getLogoId() > 0) {
                manufacturer.setManufacturerLogo(new Image(manufacturerDto.getLogoId()));
            }
            manufacturer.setManufacturerName(manufacturerDto.getManufacturerName());
            return manufacturer;
        }

    }
    
}