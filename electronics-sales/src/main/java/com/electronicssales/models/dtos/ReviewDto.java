package com.electronicssales.models.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDto {

    long id;

    int rating;

    long productId;

    CommentDto comment;
    
}