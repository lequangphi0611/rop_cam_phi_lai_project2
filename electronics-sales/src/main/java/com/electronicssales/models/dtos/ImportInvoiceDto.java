package com.electronicssales.models.dtos;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class ImportInvoiceDto {

    private long id;

    private long productId;

    @Min(1)
    private int quantity;

    private long userId;
    
}