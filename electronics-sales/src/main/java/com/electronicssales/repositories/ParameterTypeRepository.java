package com.electronicssales.repositories;

import java.util.Optional;

import com.electronicssales.entities.ParameterType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterTypeRepository extends JpaRepository<ParameterType, Long> {

    boolean existsByParameterTypeName(String parameterTypeName);

    Optional<ParameterType> findByParameterTypeName(String parameterTypeName);

}