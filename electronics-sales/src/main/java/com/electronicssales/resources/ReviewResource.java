package com.electronicssales.resources;

import java.util.concurrent.Callable;

import com.electronicssales.models.dtos.ReviewDto;
import com.electronicssales.services.ReviewService;
import com.electronicssales.utils.AuthenticateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewResource {

    @Lazy
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Callable<ResponseEntity<?>> create(@RequestBody ReviewDto review) {
        return () -> {
            long userId = AuthenticateUtils.getUserPrincipal().getId();
            review.getComment().setUserId(userId);
            return ResponseEntity
                .created(null)
                .body(reviewService.save(review));
        };
    }

    @PutMapping("/{id}")
    public Callable<ResponseEntity<?>> update(@RequestBody ReviewDto review, @PathVariable long id) {
        return () -> {
            long userId = AuthenticateUtils.getUserPrincipal().getId();
            review.getComment().setUserId(userId);
            review.setId(id);
            return ResponseEntity.ok().body(reviewService.save(review));
        };
    }

    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<?>> delete(@PathVariable long id) {
        return () -> {
            reviewService.deleteById(id);
            return ResponseEntity.ok().build();
        };
    }
    
}