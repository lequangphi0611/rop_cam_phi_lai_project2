package com.electronicssales.services.impls;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.electronicssales.entities.Comment;
import com.electronicssales.entities.User;
import com.electronicssales.models.dtos.CommentDto;
import com.electronicssales.repositories.CommentRepository;
import com.electronicssales.services.CommentService;
import com.electronicssales.utils.TwoDimensionalMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class DefaultCommentService implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Lazy
    @Autowired
    private TwoDimensionalMapper<CommentDto, Comment> commentMapper;

    @Transactional
    @Override
    public CommentDto save(CommentDto comment) {
        Comment commentTransient = commentMapper.secondMapping(comment);
        Comment commentPersisted = commentRepository.save(commentTransient);
        comment.setId(commentPersisted.getId());
        return comment;
    }

    @Transactional
    @Override
    public List<CommentDto> saveAll(Collection<CommentDto> comments) {
        return comments.stream()
            .map(this::save)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findChildsByParentId(long parentId) {
        return commentRepository.findByParentCommentId(parentId)
            .stream()
            .map(commentMapper::mapping)
            .collect(Collectors.toList());
    }

    @Lazy
    @Component
    public class CommentMapper implements TwoDimensionalMapper<CommentDto, Comment> {

        @Override
        public CommentDto mapping(Comment comment) {
            CommentDto commentDto = new CommentDto();
            commentDto.setId(comment.getId());
            Optional.ofNullable(comment.getParentComment())
                .ifPresent(parentComment -> commentDto.setParentId(parentComment.getId()));
            commentDto.setUserId(comment.getUser().getId());
            commentDto.setParagraph(comment.getParagraph());
            commentDto.setCreatedTime(comment.getCreatedTime());
            commentDto.setUpdatedTime(comment.getUpdatedTime());
            return commentDto;
        }

        @Override
        public Comment secondMapping(CommentDto commentDto) {
            Comment comment = new Comment();
            comment.setId(commentDto.getId());

            Optional.ofNullable(commentDto.getParentId())
                .ifPresent(parentId -> {
                    Comment parent = new Comment();
                    parent.setId(parentId);
                    comment.setParentComment(parent);
                });

            User user = new User();
            user.setId(commentDto.getUserId());
            comment.setUser(user);
            comment.setParagraph(commentDto.getParagraph());
            return comment;
        }
    
    }

    
}