package com.electronicssales.repositories;

import com.electronicssales.entities.ImportInvoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoice, Long> {

    
}