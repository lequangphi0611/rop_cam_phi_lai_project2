package com.electronicssales.services.impls;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.Product;
import com.electronicssales.entities.Transaction;
import com.electronicssales.entities.TransactionDetailed;
import com.electronicssales.entities.UserInfo;
import com.electronicssales.errors.ExceedTheNumberOfProductsException;
import com.electronicssales.models.TransactionDetailedProjections;
import com.electronicssales.models.TransactionFetchOption;
import com.electronicssales.models.TransactionProjections;
import com.electronicssales.models.dtos.TransactionDetailedDto;
import com.electronicssales.models.dtos.TransactionDto;
import com.electronicssales.repositories.TransactionDetailedRepository;
import com.electronicssales.repositories.TransactionRepository;
import com.electronicssales.services.TransactionService;
import com.electronicssales.utils.Mapper;
import com.electronicssales.utils.TwoDimensionalMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class DefaultTransactionService implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailedRepository transactionDetailedRepository;

    @Lazy
    @Autowired
    private TwoDimensionalMapper<TransactionDto, Transaction> transactionMapper;

    @Lazy
    @Autowired
    private TwoDimensionalMapper<TransactionDetailedDto, TransactionDetailed> transactionDetailedMapper;

    @Override
    public TransactionDto create(TransactionDto transactionDto) {
        Transaction transactionPersisted = transactionRepository.save(transactionMapper.secondMapping(transactionDto));
        List<TransactionDetailed> transactionDetaileds = transactionDto.getTransactionDetaileds().stream()
                .map(transactionDetailedMapper::secondMapping)
                .peek(element -> element.setTransaction(transactionPersisted))
                .map(this::saveTransactionDetailed)
                .collect(Collectors.toList());
        transactionPersisted.setTransactionDetaileds(transactionDetaileds);
        return transactionMapper.mapping(transactionPersisted);
    }

    private TransactionDetailed saveTransactionDetailed(TransactionDetailed transactionDetailed) {
        try {
            return transactionDetailedRepository.saveTransactionDetailed(transactionDetailed);
        } catch (ExceedTheNumberOfProductsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransactionDto> findByCustomerInfoId(long customerInfoId) {
        // transactionDetailedRepository.saveTransactionDetailed(null);
        return null;
    }

    @Override
    public List<TransactionDetailedProjections> findTransactionDetailedByTransactionId(long transactionId) {
        return this.transactionDetailedRepository.findByTransactionId(transactionId);
    }

    @Override
    public Page<TransactionProjections> fetchAll(TransactionFetchOption option, Pageable pageable) {
        return this.transactionRepository.fetchAll(option, pageable);
    }
    @Lazy
    @Component
    private class TransactionMapper implements TwoDimensionalMapper<TransactionDto, Transaction> {

        @Lazy
        @Autowired
        private Mapper<TransactionDetailedDto, TransactionDetailed> transactionDetailedMapper;

        @Override
        public TransactionDto mapping(Transaction transaction) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setCustomerInfoId(transaction.getCustomerInfo().getId());
            transactionDto.setCreatedTime(transaction.getCreatedTime());
            List<TransactionDetailedDto> transactionDetaileds = Collections.emptyList();
            if (Optional.ofNullable(transaction.getTransactionDetaileds()).isPresent()) {
                transactionDetaileds = transaction.getTransactionDetaileds()
                    .stream()
                    .map(transactionDetailedMapper::mapping)
                    .collect(Collectors.toList());
            }
            transactionDto.setTransactionDetaileds(transactionDetaileds);
            return transactionDto;
        }

        @Override
        public Transaction secondMapping(TransactionDto transactionDto) {
            Transaction transaction = new Transaction();
            UserInfo customerInfo = new UserInfo();
            customerInfo.setId(transactionDto.getCustomerInfoId());
            System.out.println(transactionDto.getCustomerInfoId());
            transaction.setCustomerInfo(customerInfo);
            return transaction;
        }

    }

    @Lazy
    @Component
    public class TransactionDetailedMapper implements TwoDimensionalMapper<TransactionDetailedDto, TransactionDetailed> {

        @Override
        public TransactionDetailedDto mapping(TransactionDetailed transactionDetailed) {
            TransactionDetailedDto transactionDetailedDto = new TransactionDetailedDto();
            transactionDetailedDto.setProductId(transactionDetailed.getProduct().getId());
            transactionDetailedDto.setDiscountType(transactionDetailed.getDiscountType());
            transactionDetailedDto.setPrice(transactionDetailed.getPrice());
            transactionDetailedDto.setDiscountValue(transactionDetailed.getDiscountValue());
            transactionDetailedDto.setQuantity(transactionDetailed.getQuantity());
            return transactionDetailedDto;
        }

        @Override
        public TransactionDetailed secondMapping(TransactionDetailedDto transactionDetailedDto) {
            TransactionDetailed transactionDetailed = new TransactionDetailed();
            transactionDetailed.setProduct(new Product(transactionDetailedDto.getProductId()));
            transactionDetailed.setDiscountType(transactionDetailedDto.getDiscountType());
            transactionDetailed.setDiscountValue(transactionDetailedDto.getDiscountValue());
            transactionDetailed.setPrice(transactionDetailedDto.getPrice());
            transactionDetailed.setQuantity(transactionDetailedDto.getQuantity());
            return transactionDetailed;
        }
    
        
    }

}