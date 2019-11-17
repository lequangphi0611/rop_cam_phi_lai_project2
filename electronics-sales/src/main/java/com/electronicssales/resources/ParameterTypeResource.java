package com.electronicssales.resources;

import java.util.concurrent.Callable;

import com.electronicssales.entities.ParameterType;
import com.electronicssales.services.ParameterTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parameter-types")
public class ParameterTypeResource {

    @Autowired
    private ParameterTypeService parameterTypeService;

    @GetMapping
    public Callable<ResponseEntity<?>> fetchAll() {
        return () -> ResponseEntity.ok(this.parameterTypeService.findAll());
    }

    @PostMapping
    public Callable<ResponseEntity<?>> create(@RequestBody ParameterType parameterType) {
        return () -> ResponseEntity
            .created(null)
            .body(this.parameterTypeService.create(parameterType));
    }
    
}