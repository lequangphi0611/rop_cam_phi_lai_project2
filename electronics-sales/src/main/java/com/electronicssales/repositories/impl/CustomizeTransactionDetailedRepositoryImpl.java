package com.electronicssales.repositories.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.electronicssales.entities.TransactionDetailed;
import com.electronicssales.errors.ExceedTheNumberOfProductsException;
import com.electronicssales.models.TransactionDetailedProjections;
import com.electronicssales.repositories.CustomizeTransactionDetailedRepository;
import com.electronicssales.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeTransactionDetailedRepositoryImpl implements CustomizeTransactionDetailedRepository {

    private static final String FIND_FIRST_IMAGE_DATA = "SELECT TOP 1 i.data AS 'data' FROM images i INNER JOIN product_images pd ON pd.image_id = i.id";

    private static final String FIND_BY_TRANSACTION_ID_QUERY = "SELECT"
        +   " td.id as id, td.price as price, td.quantity as quantity,"
        +   " td.discount_type as discountType, td.discount_value as discountValue,"
        +   " p.product_name as productName, (" + FIND_FIRST_IMAGE_DATA 
        +   " WHERE pd.product_id = p.id) as image"  
        +   " FROM transaction_detaileds td INNER JOIN products p"
        +   " ON td.product_id = p.id WHERE td.transaction_id = ?1";

    private static final String TRANSACTION_DETAILED_PROJECTIONS_MAPPING = "TransactionDetailedProjectionsMapping";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CrudRepository<TransactionDetailed, Long> crudRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Override
    public TransactionDetailed saveTransactionDetailed(TransactionDetailed transactionDetailed)
            throws ExceedTheNumberOfProductsException {
        long productId = transactionDetailed.getProduct().getId();
        final int oldProductQuantity = productRepository.getProductQuantity(productId).getQuantity();
        final int newProductQuantity = oldProductQuantity - transactionDetailed.getQuantity();
        if (newProductQuantity < 0) {
            throw new ExceedTheNumberOfProductsException(transactionDetailed.getQuantity() + " Exceed the number of Products (" + oldProductQuantity + ") !");
        }
        productRepository.updateProductQuantity(newProductQuantity, productId);
        return crudRepository.save(transactionDetailed);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TransactionDetailedProjections> findByTransactionId(long transactionId) {
        return entityManager
            .createNativeQuery(FIND_BY_TRANSACTION_ID_QUERY, TRANSACTION_DETAILED_PROJECTIONS_MAPPING)
            .setParameter(1, transactionId)
            .getResultList();
    }

}