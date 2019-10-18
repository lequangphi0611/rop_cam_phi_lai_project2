package com.electronicssales.services.impls;

import com.electronicssales.entities.ImportInvoice;
import com.electronicssales.entities.Product;
import com.electronicssales.entities.User;
import com.electronicssales.models.dtos.ImportInvoiceDto;
import com.electronicssales.repositories.ImportInvoiceRepository;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.repositories.UserRepository;
import com.electronicssales.services.ImportInvoiceService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class DefaultImportInvoiceService implements ImportInvoiceService {

    @Lazy
    @Autowired
    private ImportInvoiceRepository importInvoiceRepository;

    @Lazy
    @Autowired
    private ProductRepository productRepository;

    @Lazy
    @Autowired
    private Mapper<ImportInvoice, ImportInvoiceDto> importInvoiceMapper;

    @Override
    @Transactional
    public ImportInvoice saveImportInvoice(ImportInvoiceDto importInvoiceDto) {
        ImportInvoice importTransient = importInvoiceMapper.mapping(importInvoiceDto);
        ImportInvoice importInvoiceSaved = importInvoiceRepository.save(importTransient);

        productRepository.updateProductQuantity(
                importInvoiceSaved.getQuantity(), 
                importInvoiceSaved.getProduct().getId()
        );

        return importInvoiceSaved;
    }
    @Lazy
    @Component
    public class ImportInvoiceMapper implements Mapper<ImportInvoice, ImportInvoiceDto> {

        @Lazy
        @Autowired 
        private UserRepository userRepository;

        @Override
        public ImportInvoice mapping(ImportInvoiceDto importInvoiceDto) {
            ImportInvoice importInvoice = new ImportInvoice();
            User creator = new User();
            creator.setId(importInvoiceDto.getUserId());
            importInvoice.setCreator(creator);
            importInvoice.setQuantity(importInvoiceDto.getQuantity());
            importInvoice.setProduct(new Product(importInvoiceDto.getProductId()));
            return importInvoice;
        }
        
    }
    
}