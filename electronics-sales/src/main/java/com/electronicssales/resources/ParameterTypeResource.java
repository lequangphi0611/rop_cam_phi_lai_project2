package com.electronicssales.resources;

import java.util.Optional;
import java.util.concurrent.Callable;

import com.electronicssales.entities.ParameterType;
import com.electronicssales.services.ParameterTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.lang.Strings;

@RestController
@RequestMapping("/api/parameter-types")
public class ParameterTypeResource {

    @Autowired
    private ParameterTypeService parameterTypeService;

    @GetMapping
    public Callable<ResponseEntity<?>> fetchAll(
        @RequestParam(required = false) String name
    ) {
        return () ->  ResponseEntity.ok(this.parameterTypeService.findAll( Strings.hasText(name) ? name : null));
    }

    @PostMapping
    public Callable<ResponseEntity<?>> create(@RequestBody ParameterType parameterType) {
        return () -> {
            Optional<ParameterType> parameterFinded = parameterTypeService.findByName(parameterType.getParameterTypeName());
            if(parameterFinded.isPresent()) {
                return parameterFinded
                    .map(p -> ParameterType.of(p.getId(), p.getParameterTypeName()))
                    .map(ResponseEntity::ok).get();
            }

            return ResponseEntity
                .created(null)
                .body(this.parameterTypeService.create(parameterType));
        };
    }
    
}