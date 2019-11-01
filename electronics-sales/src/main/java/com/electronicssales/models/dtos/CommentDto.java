package com.electronicssales.models.dtos;

import java.util.Date;

import com.electronicssales.entities.Paragraph;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    long id;

    Long parentId;

    Long userId;

    Paragraph paragraph;

    Date createdTime;

    Date updatedTime;
    
}