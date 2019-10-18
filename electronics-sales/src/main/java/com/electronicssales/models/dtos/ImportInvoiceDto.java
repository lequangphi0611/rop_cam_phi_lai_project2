package com.electronicssales.models.dtos;

import lombok.Data;

@Data
public class ImportInvoiceDto {

    private long id;

    private long productId;

    private int quantity;

    private long userId;
    
}