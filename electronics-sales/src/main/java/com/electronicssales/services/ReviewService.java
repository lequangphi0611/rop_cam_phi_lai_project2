package com.electronicssales.services;

import java.util.List;

import com.electronicssales.models.dtos.ReviewDto;

public interface ReviewService {

    ReviewDto save(ReviewDto reviewDto);

    void deleteById(long reviewId);

    List<ReviewDto> findByProductId(long productId);
    
}