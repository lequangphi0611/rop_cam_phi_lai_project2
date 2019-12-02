package com.electronicssales.models;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountFetchOption {

    Date fromDate;

    Date toDate;
    
}