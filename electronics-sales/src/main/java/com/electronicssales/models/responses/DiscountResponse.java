package com.electronicssales.models.responses;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountResponse {

    long id;

    long discountValue;

    String discountType;

    Date startedTime;
    
}