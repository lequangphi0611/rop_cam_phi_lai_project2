package com.electronicssales.models.dtos;

import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDto {

    long id;

    Date createdTime;

    long customerInfoId;

    List<TransactionDetailedDto> transactionDetaileds;
    
}