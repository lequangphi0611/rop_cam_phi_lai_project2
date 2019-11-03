package com.electronicssales.services;

import java.util.List;

import com.electronicssales.models.dtos.ReviewDto;

import org.springframework.data.domain.Pageable;

public interface ReviewService {

    ReviewDto save(ReviewDto reviewDto);

    void deleteById(long reviewId);

    List<ReviewDto> findByProductId(long productId, Pageable pageable);
    
}