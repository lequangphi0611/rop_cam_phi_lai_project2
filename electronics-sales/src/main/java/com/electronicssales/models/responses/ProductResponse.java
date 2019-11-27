package com.electronicssales.models.responses;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    long id;

    String productName;

    long price;

    int quantity;

    Long manufacturerId;

    Date createdTime;

    Date updatedTime;

}