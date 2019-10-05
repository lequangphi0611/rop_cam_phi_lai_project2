package com.electronicssales.repositories;

import com.electronicssales.entities.ImportInvoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoice, Long> {

    String UPDATE_CREATOR_ID_QUERY = "UPDATE import_invoices SET creator_id = ?1 WHERE id = ?2";

    @Modifying
    @Query(value = UPDATE_CREATOR_ID_QUERY, nativeQuery = true)
    void updateCreatorId(long creatorId, long importInvoiceId);
    
}