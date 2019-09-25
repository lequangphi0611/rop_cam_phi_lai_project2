package com.electronicssales.models.responses;

import lombok.Data;

@Data
public class ProductParameterResponse {

    private long productParameterId;

    private String parameterType;

    private String parameterValue;
    
}