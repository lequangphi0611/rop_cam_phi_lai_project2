package com.electronicssales.resources;

import java.util.concurrent.Callable;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.electronicssales.entities.Manufacturer;
import com.electronicssales.models.dtos.ManufacturerDto;
import com.electronicssales.services.ManufacturerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerResource {

    @Lazy
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
    public Callable<ResponseEntity<?>> createManufacturer(
            @RequestPart ManufacturerDto manufacturer, 
            @RequestPart(required = false) MultipartFile image) 
    {
        return () -> {
            final String manufacturerName = manufacturer.getManufacturerName();
            if(manufacturerService.existsByManufacturerName(manufacturerName)) {
                StringBuilder builder = new StringBuilder(Manufacturer.class.getSimpleName());
                builder
                    .append(" with name = '")
                    .append(manufacturerName)
                    .append("' is already exists !");
                throw new EntityExistsException(builder.toString());
            }
            manufacturer.setImage(image);
            return ResponseEntity
                .created(null)
                .body(manufacturerService.save(manufacturer));
        };
    }

    @PutMapping("/{id}")
    public Callable<ResponseEntity<?>> updateManufacturer(
        @RequestPart ManufacturerDto manufacturer,
        @RequestPart(required = false) MultipartFile image,
        @PathVariable long id
    ) 
    {
        return () -> {
            Manufacturer manufacturerPersisted = manufacturerService
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found !"));

            if(!manufacturerPersisted.getManufacturerName().equalsIgnoreCase(manufacturer.getManufacturerName())
                && manufacturerService.existsByManufacturerName(manufacturer.getManufacturerName())) {
                    throw new EntityExistsException("Manufacturer Name is already exists !");
            }
            manufacturer.setId(id);
            manufacturer.setImage(image);
            return ResponseEntity.ok(manufacturerService.save(manufacturer));
        };
    }

    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<?>> deleteManufacturer(@PathVariable long id) {
        return () -> {
            requiredManufacturerId(id);
            manufacturerService.deleteById(id);
            return ResponseEntity.ok().build();
        };
    }
    
}