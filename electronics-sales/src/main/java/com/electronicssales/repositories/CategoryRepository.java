package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.electronicssales.entities.Category;
import com.electronicssales.models.CategoryIdAndNameOnly;
import com.electronicssales.models.responses.ICategoryReponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MyCustomizeRepository<Category, Long> {

    String DELETE_CATEGORY_PARAMETERS_BY_CATEGORY_ID = "DELETE FROM categories_parameter_types"
            + " WHERE category_id = ?1";

    String FETCH_CATEGORIES = "SELECT c.id, c.category_name AS categoryName, c.parent_id as parentId"
            + ", COUNT(pc.id) AS productCount FROM categories c" + " LEFT OUTER JOIN product_categories pc"
            + " ON c.id = pc.category_id WHERE c.category_name LIKE %:nameKeyword%"
            + " GROUP BY c.id, c.category_name, c.parent_id";

    String FETCH_CHILDRENS_BY_PARENT_ID_QUERY = "SELECT c.id, c.category_name AS categoryName, c.parent_id as parentId"
            + ", COUNT(pc.id) AS productCount FROM categories c" + " LEFT OUTER JOIN product_categories pc"
            + " ON c.id = pc.category_id WHERE c.parent_id = :parentId " + " AND c.category_name LIKE %:nameKeyword%"
            + " GROUP BY c.id, c.category_name, c.parent_id";

    String FETCH_CATEGORIES_HAS_PRODUCT_SELLABLE = "SELECT c.id, c.category_name AS categoryName, c.parent_id as parentId, COUNT(pc.id) AS productCount" 
        +   " FROM categories c INNER JOIN product_categories pc"
        +   " ON c.id = pc.category_id INNER JOIN products p on p.id = pc.product_id"
        +   " WHERE p.status = 0 AND p.quantity > 0"
        +   " GROUP BY c.id, c.category_name, c.parent_id"
        +   " ORDER BY COUNT(pc.id) desc";

    String FETCH_ALL_CATEGORIES = "SELECT c FROM Category c WHERE c.categoryName like %:nameKeyword%";

    String HAS_CHILDRENS_QUERY = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.parent.id = ?1";

    String FIND_BY_PRODUCT_ID = "SELECT c FROM Category c JOIN c.productCategories pc WHERE pc.product.id = ?1";

    String DELETE_ALL_CATEGORY_PARAMETER_TYPES_BYCATEGORY = "DELETE FROM categories_parameter_types WHERE category_id = ?1";

    String FIND_PARENT_ONLY = "SELECT c.id as id, c.categoryName as name FROM Category c WHERE c.parent.id IS NULL";

    boolean existsByCategoryName(String categoryName);

    @Override
    @Query("SELECT c FROM Category c ORDER BY c.categoryName ASC")
    List<Category> findAll();

    @Query(FETCH_ALL_CATEGORIES)
    List<Category> findAll(@Param("nameKeyword") String nameKeyword);

    @Query(FETCH_ALL_CATEGORIES)
    List<Category> findAll(Pageable pageable, @Param("nameKeyword") String nameKeyword);

    @Query(value = FETCH_CATEGORIES, nativeQuery = true)
    List<ICategoryReponse> fetchCategoriesNotHasParent(@Param(value = "nameKeyword") String nameKeyword);

    @Query(nativeQuery = true, value = FETCH_CATEGORIES_HAS_PRODUCT_SELLABLE)
    Set<ICategoryReponse> fetchCategoriesHasProductSellable();

    @Query(value = FETCH_CHILDRENS_BY_PARENT_ID_QUERY, nativeQuery = true)
    List<ICategoryReponse> fetchChildrensOf(@Param(value = "parentId") long parentId,
            @Param(value = "nameKeyword") String nameKeyword);

    List<Category> findByParentId(long parentId);

    Optional<Category> findByCategoryName(String categoryName);

    int countByParentId(long parentId);

    @Query(value = HAS_CHILDRENS_QUERY)
    boolean hasChildrens(long categoryId);

    @Modifying
    @Query(value = DELETE_CATEGORY_PARAMETERS_BY_CATEGORY_ID, nativeQuery = true)
    void deleteCategoryParametersByCategoryId(long categoryId);

    @Query(FIND_BY_PRODUCT_ID)
    List<Category> findByProductId(long productId);

    @Query(value = DELETE_ALL_CATEGORY_PARAMETER_TYPES_BYCATEGORY, nativeQuery = true)
    @Modifying
    void deleteAllCategoryParameterTypeBy(long categoryId);

    @Query(FIND_PARENT_ONLY)
    List<CategoryIdAndNameOnly> findParentOnly(); 

}