package com.electronicssales.models.responses;

import java.util.List;

import com.electronicssales.models.types.FetchProductType;
import com.electronicssales.models.types.ProductSortType;
import com.electronicssales.models.types.SortType;

import org.springframework.data.domain.Pageable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FetchProductOption {

    List<Long> categoriesId;

    List<Long> manufacturersId;

    long fromPrice;

    long toPrice;

    ProductSortType productSortType;

    SortType sortType = SortType.ASC;

    Pageable pageable;

    String searchKey;

    FetchProductType fetchProductType;
    
}