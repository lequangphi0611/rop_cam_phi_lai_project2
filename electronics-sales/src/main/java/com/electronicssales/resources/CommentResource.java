package com.electronicssales.resources;

import java.util.concurrent.Callable;

import com.electronicssales.models.dtos.CommentDto;
import com.electronicssales.services.CommentService;
import com.electronicssales.utils.AuthenticateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentResource {

    @Lazy
    @Autowired
    private CommentService commentService;

    @PostMapping
    public Callable<ResponseEntity<?>> create(@RequestBody CommentDto commentDto) {
        return () -> {
            long userId = AuthenticateUtils.getUserPrincipal().getId();
            commentDto.setUserId(userId);
            return ResponseEntity.created(null).body(commentService.save(commentDto));
        };
    }

    @GetMapping("/{id}/childrens")
    public Callable<ResponseEntity<?>> fetchChildrens(@PathVariable long id,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false) Integer size) {
        final Pageable pageable = (size == null || size <= 0) ? null : PageRequest.of(page, size);
        return () -> ResponseEntity.ok(commentService.findChildsByParentId(id, pageable));
    }

    @PutMapping("/{id}")
    public Callable<ResponseEntity<?>> update(@PathVariable long id, @RequestBody CommentDto commentDto) {
        commentDto.setId(id);
        return create(commentDto);
    }

    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<?>> delete(@PathVariable long id) {
        return () -> {
            commentService.deleteById(id);
            return ResponseEntity.ok().build();
        };
    }

}