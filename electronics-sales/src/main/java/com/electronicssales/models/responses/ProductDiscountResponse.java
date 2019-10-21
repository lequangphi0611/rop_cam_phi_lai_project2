package com.electronicssales.models.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductDiscountResponse extends ProductResponse {

    private DiscountResponse discount;
    
} 