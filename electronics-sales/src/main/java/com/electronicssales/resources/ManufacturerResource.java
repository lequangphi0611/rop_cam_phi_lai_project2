package com.electronicssales.resources;

import java.util.Optional;
import java.util.concurrent.Callable;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.electronicssales.entities.Manufacturer;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.models.responses.ManufacturerResponse;
import com.electronicssales.services.ManufacturerService;
import com.electronicssales.utils.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerResource {

    @Lazy
    @Autowired
    private ManufacturerService manufacturerService;

    @Lazy
    @Autowired
    private Mapper<ManufacturerResponse, Manufacturer> manufacturerMapper;

    @Lazy
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public Callable<ResponseEntity<?>> fetchManufacturers(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", required = false) Integer size) {
        return () -> {
            if(size == null) {
                return ResponseEntity.ok(manufacturerService.findAll());
            }
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(manufacturerService.findAll(pageable));
        };
    }

    @GetMapping("/{id}")
    public Callable<ResponseEntity<?>> getManufacturer(@PathVariable long id) {
        return () -> ResponseEntity.ok(manufacturerService.findById(id).map(manufacturerMapper::mapping).get());
    }
    
    @PostMapping(consumes = { "multipart/form-data" })
    public Callable<ResponseEntity<?>> createManufacturer(
            @RequestParam("manufacturer") String manufacturerStr, 
            @RequestParam(value="logo", required = false) MultipartFile image) 
    {
        return () -> {
            ManufacturerDto manufacturer = this.objectMapper.readValue(manufacturerStr, ManufacturerDto.class);
            final String manufacturerName = manufacturer.getManufacturerName();
            if(manufacturerService.existsByManufacturerName(manufacturerName)) {
                StringBuilder builder = new StringBuilder(Manufacturer.class.getSimpleName());
                builder
                    .append(" with name = '")
                    .append(manufacturerName)
                    .append("' is already exists !");
                throw new EntityExistsException(builder.toString());
            }
            Optional.ofNullable(image).ifPresent(manufacturer::setImage);
            return ResponseEntity
                .created(null)
                .body(manufacturerService.save(manufacturer));
        };
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public Callable<ResponseEntity<?>> updateManufacturer(
        @RequestParam("manufacturer") String manufacturerStr,
        @RequestParam(value="logo", required = false) MultipartFile image,
        @PathVariable long id
    ) 
    {
        return () -> {
            Manufacturer manufacturerPersisted = manufacturerService
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found !"));
            ManufacturerDto manufacturer = this.objectMapper.readValue(manufacturerStr, ManufacturerDto.class);
            if(!manufacturerPersisted.getManufacturerName().equalsIgnoreCase(manufacturer.getManufacturerName())
                && manufacturerService.existsByManufacturerName(manufacturer.getManufacturerName())) {
                    throw new EntityExistsException("Manufacturer Name is already exists !");
            }
            manufacturer.setId(id);
            Optional.ofNullable(image).ifPresent(manufacturer::setImage);
            return ResponseEntity.ok(manufacturerService.save(manufacturer));
        };
    }

    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<?>> deleteManufacturer(@PathVariable long id) {
        return () -> {
            manufacturerService.deleteById(id);
            return ResponseEntity.ok().build();
        };
    }
    
}