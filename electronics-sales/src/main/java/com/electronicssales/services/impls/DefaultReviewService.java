package com.electronicssales.services.impls;

import java.util.List;
import java.util.stream.Collectors;

import com.electronicssales.entities.Comment;
import com.electronicssales.entities.Product;
import com.electronicssales.entities.Review;
import com.electronicssales.models.dtos.CommentDto;
import com.electronicssales.models.dtos.ReviewDto;
import com.electronicssales.models.types.Rating;
import com.electronicssales.repositories.ReviewRepository;
import com.electronicssales.services.ReviewService;
import com.electronicssales.utils.TwoDimensionalMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class DefaultReviewService implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Lazy
    @Autowired
    private TwoDimensionalMapper<ReviewDto, Review> reviewMapper;

    @Transactional
    @Override
    public ReviewDto save(ReviewDto reviewDto) {
        Review reviewPersisted = reviewRepository.save(reviewMapper.secondMapping(reviewDto));
        return reviewMapper.mapping(reviewPersisted);
    }

    @Transactional
    @Override
    public void deleteById(long reviewId) {
        reviewRepository.deleteById(reviewId);    
    }

    @Override
    public List<ReviewDto> findByProductId(long productId, Pageable pageable) {
        return reviewRepository
            .findByProductId(productId, pageable) 
            .stream()
            .map(reviewMapper::mapping)
            .collect(Collectors.toList());
    }

    @Lazy
    @Component
    public class ReviewMapper implements TwoDimensionalMapper<ReviewDto, Review> {

        @Lazy
        @Autowired
        private TwoDimensionalMapper<CommentDto, Comment> commentMapper;

        @Override
        public ReviewDto mapping(Review review) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(review.getId());
            reviewDto.setProductId(review.getProduct().getId());
            reviewDto.setRating(review.getRating().getValue());
            reviewDto.setComment(commentMapper.mapping(review.getComment()));
            return reviewDto;
        }

        @Override
        public Review secondMapping(ReviewDto reviewDto) {
            Review review = new Review();
            review.setId(reviewDto.getId());
            review.setProduct(new Product(reviewDto.getProductId()));
            review.setRating(Rating.of(reviewDto.getRating()));
            review.setComment(commentMapper.secondMapping(reviewDto.getComment()));
            return review;
        }
        
    }
    
}