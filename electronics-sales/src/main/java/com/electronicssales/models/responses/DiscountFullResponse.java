package com.electronicssales.models.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DiscountFullResponse extends DiscountResponse {

    private long productCount;
    
}