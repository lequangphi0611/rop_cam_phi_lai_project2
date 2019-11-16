package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Manufacturer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends MyCustomizeRepository<Manufacturer, Long> {

    String FIND_ALL_QUERY = "SELECT m FROM Manufacturer m ORDER BY id DESC";

    String FIND_BY_CATEGORY_ID = "SELECT m FROM Manufacturer m"
        +   " JOIN m.categoryManufacturers cm"
        +   " WHERE cm.category.id = ?1";

    @Override
    @Query(FIND_ALL_QUERY)
    List<Manufacturer> findAll();

    @Override
    @Query(FIND_ALL_QUERY)
    Page<Manufacturer> findAll(Pageable pageable);

    boolean existsByManufacturerName(String manufacturerName);

    Optional<Manufacturer> findByManufacturerName(String manufacturerName);

    @Query(FIND_BY_CATEGORY_ID)
    List<Manufacturer> findByCategoryId(long categoryId);

}