package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.entities.Comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    String FIND_BY_PARENT_COMMENT_ID = "SELECT c FROM Comment c"
        +   " JOIN FETCH c.paragraph p"
        +   " WHERE c.parentComment.id = ?1"
        +   " ORDER BY c.createdTime DESC";

    @Query(FIND_BY_PARENT_COMMENT_ID)
    List<Comment> findByParentCommentId(long parentId, Pageable pageable);
    
}