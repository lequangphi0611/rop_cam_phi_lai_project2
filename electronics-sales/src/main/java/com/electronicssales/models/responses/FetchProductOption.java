package com.electronicssales.models.responses;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FetchProductOption {

    List<Long> categoriesId;

    List<Long> manufacturersId;
    
}