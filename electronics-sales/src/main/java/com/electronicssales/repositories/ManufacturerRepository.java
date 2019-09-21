package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    @Override
    @Query("SELECT m FROM Manufacturer m ORDER BY manufacturerName ASC")
    List<Manufacturer> findAll();

    boolean existsByManufacturerName(String manufacturerName);

    Optional<Manufacturer> findByManufacturerName(String manufacturerName);

}