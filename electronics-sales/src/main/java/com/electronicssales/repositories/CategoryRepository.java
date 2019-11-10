package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Category;
import com.electronicssales.models.responses.ICategoryReponse;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MyCustomizeRepository<Category, Long> {

    String DELETE_CATEGORY_PARAMETERS_BY_CATEGORY_ID = "DELETE FROM categories_parameter_types"
        +   " WHERE category_id = ?1";

    String FETCH_CATEGORIES_NOT_HAS_PARENT = "SELECT c.id, c.category_name AS categoryName, 0 AS parentId" 
        +   ", COUNT(pc.id) AS productCount FROM categories c"
        +   " LEFT OUTER JOIN product_categories pc" 
        +   " ON c.id = pc.category_id WHERE c.parent_id IS NULL"
        +   " AND c.category_name LIKE %:nameKeyword%"
        +   " GROUP BY c.id, c.category_name, c.parent_id";

    String FETCH_CHILDRENS_BY_PARENT_ID_QUERY = "SELECT c.id, c.category_name AS categoryName, c.parent_id as parentId" 
    +   ", COUNT(pc.id) AS productCount FROM categories c"
    +   " LEFT OUTER JOIN product_categories pc" 
    +   " ON c.id = pc.category_id WHERE c.parent_id = :parentId "
    +   " AND c.category_name LIKE %:nameKeyword%"
    +   " GROUP BY c.id, c.category_name, c.parent_id";

    String HAS_CHILDRENS_QUERY = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.parent.id = ?1";

    boolean existsByCategoryName(String categoryName);   

    @Override
    @Query("SELECT c FROM Category c ORDER BY c.categoryName ASC")
    List<Category> findAll();

    @Query(value = FETCH_CATEGORIES_NOT_HAS_PARENT, nativeQuery = true)
    List<ICategoryReponse> fetchCategoriesNotHasParent(@Param(value = "nameKeyword") String nameKeyword);

    @Query(value = FETCH_CHILDRENS_BY_PARENT_ID_QUERY, nativeQuery = true)
    List<ICategoryReponse> fetchChildrensOf(
        @Param(value = "parentId") long parentId, 
        @Param(value = "nameKeyword") String nameKeyword);

    List<Category> findByParentId(long parentId); 

    Optional<Category> findByCategoryName(String categoryName);

    int countByParentId(long parentId);

    @Query(value = HAS_CHILDRENS_QUERY)
    boolean hasChildrens(long categoryId);

    @Modifying
    @Query(
        value = DELETE_CATEGORY_PARAMETERS_BY_CATEGORY_ID,
        nativeQuery = true
    )
    void deleteCategoryParametersByCategoryId(long categoryId);
    
}