package com.electronicssales.services;

import java.util.Collection;
import java.util.List;

import com.electronicssales.models.dtos.CommentDto;

public interface CommentService {

    CommentDto save(CommentDto comment);

    List<CommentDto> saveAll(Collection<CommentDto> comments);

    void deleteById(long commentId);
    
    List<CommentDto> findChildsByParentId(long parentId);
}