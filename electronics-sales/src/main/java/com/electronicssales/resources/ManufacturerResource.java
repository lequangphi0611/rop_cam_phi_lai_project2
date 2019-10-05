package com.electronicssales.resources;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.electronicssales.entities.Manufacturer;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.services.ManufacturerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerResource {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping
    public ResponseEntity<?> fetchManufacturers() {
        return ResponseEntity.ok(manufacturerService.findAll());
    }

    private boolean requiredManufacturerId(long productId) {
        if(manufacturerService.existsById(productId)) {
            return true;
        }

        throw new EntityNotFoundException(Manufacturer.class.getSimpleName() + " with id not found !");
    }

    @PostMapping
    public ResponseEntity<?> createManufacturer(@RequestBody @Valid ManufacturerDto manufacturerDto) {
        final String manufacturerName = manufacturerDto.getManufacturerName();
        if(manufacturerService.existsByManufacturerName(manufacturerName)) {
            StringBuilder builder = new StringBuilder(Manufacturer.class.getSimpleName());
            builder
                .append(" with name = '")
                .append(manufacturerName)
                .append("' is already exists !");
            throw new EntityExistsException(builder.toString());
        }
        Manufacturer manufacturerSaved = manufacturerService.save(manufacturerDto);
        return ResponseEntity
            .created(null)
            .body(manufacturerSaved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createBulk(@RequestBody @Valid Collection<ManufacturerDto> manufacturerDtos) {
        Collection<ManufacturerDto> manufacturers = manufacturerDtos
            .stream()
            .filter(manufacturerDto -> !manufacturerService.existsByManufacturerName(manufacturerDto.getManufacturerName()))
            .collect(Collectors.toList());
        return ResponseEntity
            .created(null)
            .body(manufacturerService.saveAll(manufacturers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateManufacturer(
        @RequestBody @Valid ManufacturerDto manufacturerDto,
        @PathVariable long id
    ) 
    {
        Manufacturer manufacturer = manufacturerService
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found !"));

        if(!manufacturer.getManufacturerName().equalsIgnoreCase(manufacturerDto.getManufacturerName())
            && manufacturerService.existsByManufacturerName(manufacturerDto.getManufacturerName())) {
                throw new EntityExistsException("Manufacturer Name is already exists !");
        }
        manufacturerDto.setId(id);
        Manufacturer newManufacturer = manufacturerService.save(manufacturerDto);
        return ResponseEntity.ok(newManufacturer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable long id) {
        requiredManufacturerId(id);
        manufacturerService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
}