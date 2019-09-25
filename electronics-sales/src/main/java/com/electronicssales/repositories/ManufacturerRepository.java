package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    String FIND_ALL_QUERY = "SELECT m FROM Manufacturer m ORDER BY manufacturerName ASC";

    String FIND_BY_CATEGORY_ID_QUERY = "SELECT manufacturer.id, manufacturer.manufacturer_name, manufacturer.logo_id"
        +   " FROM manufacturers manufacturer"
        +   " INNER JOIN products product"
        +   " ON manufacturer.id = product.manufacturer_id"
        +   " WHERE product.category_id = :categoryId"
        +   " GROUP BY manufacturer.id,  manufacturer.manufacturer_name, manufacturer.logo_id"
        +   " ORDER BY manufacturer.manufacturer_name ASC";

    @Override
    @Query(FIND_ALL_QUERY)
    List<Manufacturer> findAll();

    boolean existsByManufacturerName(String manufacturerName);

    Optional<Manufacturer> findByManufacturerName(String manufacturerName);

    @Query(value = FIND_BY_CATEGORY_ID_QUERY, nativeQuery = true)
    List<Manufacturer> findByCategoryId(@Param("categoryId") long categoryId);

}