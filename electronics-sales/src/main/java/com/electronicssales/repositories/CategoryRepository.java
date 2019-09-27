package com.electronicssales.repositories;

import java.util.Collection;
import java.util.List;

import com.electronicssales.entities.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategoryName(String categoryName);   

    @Override
    @Query("SELECT c FROM Category c ORDER BY c.categoryName ASC")
    List<Category> findAll();

    Page<Category> findByCategoryNameContaining(String categoryName, Pageable pageable);

    Collection<Category> findByCategoryNameContaining(String query);

    List<Category> findByParentId(long parentId); 

    int countByParentId(long parentId);
    
}