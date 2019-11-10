package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.Image;
import com.electronicssales.entities.Manufacturer;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.models.responses.ManufacturerResponse;
import com.electronicssales.repositories.ManufacturerRepository;
import com.electronicssales.services.ManufacturerService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class DefaultManufacturerService implements ManufacturerService {

    @Lazy
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Lazy
    @Autowired
    private Mapper<Manufacturer, ManufacturerDto> manufacturerMapper;

    @Lazy
    @Autowired
    private Mapper<ManufacturerResponse, Manufacturer> manufacturerResponseMapper;

    @Override
    public Collection<ManufacturerResponse> findAll() {
        return manufacturerRepository.findAll()
            .stream()
            .map(manufacturerResponseMapper::mapping)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ManufacturerResponse save(ManufacturerDto manufacturerDto) {
        return Optional.of(manufacturerRepository.save(manufacturerMapper.mapping(manufacturerDto)))
            .map(manufacturerResponseMapper::mapping)
            .get();
    }

    @Transactional
    @Override
    public Collection<ManufacturerResponse> saveAll(Collection<ManufacturerDto> manufacturerDtos) {
        return manufacturerDtos.stream()
                .map(manufacturerMapper::mapping)
                .map(manufacturerRepository::save)
                .map(manufacturerResponseMapper::mapping)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean existsByManufacturerName(String manufacturerName) {
        return manufacturerRepository.existsByManufacturerName(manufacturerName);
    }

    @Transactional
    @Override
    public Optional<Manufacturer> findByManufacturerName(String manufacturerName) {
        return manufacturerRepository.findByManufacturerName(manufacturerName);
    }

    @Transactional
    @Override
    public Optional<Manufacturer> findById(long id) {
        return manufacturerRepository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return existsById(id);
    }

    @Override
    public void deleteById(long id) {
        manufacturerRepository.deleteById(id);
    }

    @Override
    public List<ManufacturerResponse> findByCategoryId(long categoryId) {
        return manufacturerRepository.findByCategoryId(categoryId)
            .stream()
            .map(manufacturerResponseMapper::mapping)
            .collect(Collectors.toList());
    }

    @Lazy
    @Component
    class ManufacturerMapper implements Mapper<Manufacturer, ManufacturerDto> {

        @Override
        public Manufacturer mapping(ManufacturerDto manufacturerDto) {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setId(manufacturerDto.getId());
            manufacturer.setManufacturerLogo(Image.of(manufacturerDto.getImage()));
            manufacturer.setManufacturerName(manufacturerDto.getManufacturerName());
            return manufacturer;
        }

    }

    @Lazy
    @Component
    private class ManufacturerResponseMapper implements Mapper<ManufacturerResponse, Manufacturer> {

        @Override
        public ManufacturerResponse mapping(Manufacturer manufacturer) {
            ManufacturerResponse manufacturerResponse = new ManufacturerResponse();
            manufacturerResponse.setId(manufacturer.getId());
            manufacturerResponse.setImageId(manufacturer.getManufacturerLogo().getId());
            manufacturerResponse.setManufacturerName(manufacturer.getManufacturerName());
            return manufacturerResponse;
        }
    
    }
    
}