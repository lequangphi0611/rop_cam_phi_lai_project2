package com.electronicssales.models.responses;

import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageDataResponse {

    byte[] data;
    
}