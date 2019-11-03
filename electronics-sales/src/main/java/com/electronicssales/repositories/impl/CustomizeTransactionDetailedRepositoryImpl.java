package com.electronicssales.repositories.impl;

import com.electronicssales.entities.TransactionDetailed;
import com.electronicssales.errors.ExceedTheNumberOfProductsException;
import com.electronicssales.repositories.CustomizeTransactionDetailedRepository;
import com.electronicssales.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeTransactionDetailedRepositoryImpl implements CustomizeTransactionDetailedRepository {

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

}