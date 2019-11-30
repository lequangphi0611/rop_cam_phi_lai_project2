package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.electronicssales.entities.Product;
import com.electronicssales.models.ProductNameAndIdOnly;
import com.electronicssales.models.responses.IParagraphResponse;
import com.electronicssales.models.responses.ProductImageOnly;
import com.electronicssales.models.responses.ProductQuantityOnly;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MyCustomizeRepository<Product, Long>, CustomizeProductRepository {

    String APPEND_PRODUCT_QUANTITY_QUERY = "UPDATE Product p" + " SET p.quantity = :newQuantity"
            + " WHERE p.id = :productId";

    String REMOVE_ALL_DISCOUNT_QUERY = "UPDATE products" + " SET discount_id = null WHERE discount_id = ?1";

    String FIND_BY_ID_QUERY = "SELECT p FROM Product p LEFT JOIN FETCH p.discount d WHERE p.id = ?1";

    String FIND_BY_ID_WITHOUT_FETCH_DISCOUNT = "SELECT p FROM Product p WHERE p.id = ?1";

    String FIND_BY_DISCOUNT_ID = "SELECT p FROM Product p JOIN p.discount d WHERE d.id = ?1"
            + " ORDER BY p.createdTime";

    String FIND_PARAGRAPHS_BY_PRODUCT_ID_NATIVE_QUERY = "SELECT" + " pg.id as id, pg.text as text, pg.title as title"
            + ", pg.image_id as imageId FROM products p" + " INNER JOIN product_descriptions pd ON p.id = pd.product_id"
            + " INNER JOIN paragraphs pg ON pd.paragraph_id = pg.id" + " WHERE p.id = ?1";

    String GET_PRODUCT_QUANTITY = "SELECT p.quantity as quantity FROM products p" + " WHERE p.id = ?1";

    String FIND_IMAGE_BY_PRODUCT_ID = "SELECT p FROM Product p" + " JOIN FETCH p.images WHERE p.id = ?1";

    String FIND_ALL_BY_DISCOUNT_NOT_AVAILABLE_QUERY = "SELECT p.id as id,"
            + " p.productName as name, pc.category.id as categoriesId" 
            + " FROM Product p JOIN p.productCategories pc WHERE pc.category.id = ?1 AND p.discount.id IS NULL";

    Optional<Product> findByProductName(String productName);

    boolean existsByProductName(String productName);

    @Modifying
    @Query(value = APPEND_PRODUCT_QUANTITY_QUERY)
    void updateProductQuantity(@Param("newQuantity") int newQuantity, @Param("productId") long productId);

    @Modifying
    @Query(value = REMOVE_ALL_DISCOUNT_QUERY, nativeQuery = true)
    void removeAllDiscount(long discountId);

    @Override
    @Query(value = FIND_BY_ID_QUERY)
    Optional<Product> findById(Long id);

    @Query(value = FIND_BY_ID_WITHOUT_FETCH_DISCOUNT)
    Optional<Product> findByIdWithoutFetchDiscount(long id);

    @Query(value = FIND_BY_DISCOUNT_ID)
    List<Product> findByDiscountId(long discountId);

    int countByDiscountId(long discountId);

    @Query(value = FIND_PARAGRAPHS_BY_PRODUCT_ID_NATIVE_QUERY, nativeQuery = true)
    List<IParagraphResponse> findParagraphsByProductId(long productId);

    @Query(value = GET_PRODUCT_QUANTITY, nativeQuery = true)
    ProductQuantityOnly getProductQuantity(long productId);

    @Query(FIND_IMAGE_BY_PRODUCT_ID)
    ProductImageOnly findImageByProductId(long productId);

    @Query(FIND_ALL_BY_DISCOUNT_NOT_AVAILABLE_QUERY)
    Set<ProductNameAndIdOnly> findAllByDiscountNotAvailable(long categoryId);

}