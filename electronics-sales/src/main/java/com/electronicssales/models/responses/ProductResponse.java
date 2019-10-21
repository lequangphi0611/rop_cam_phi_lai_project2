package com.electronicssales.models.responses;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    long id;

    String productName;

    Collection<Long> imageIds;

    long price;

    int quantity;

    Collection<Long> categoryIds;

    long manufacturerId;
    
}