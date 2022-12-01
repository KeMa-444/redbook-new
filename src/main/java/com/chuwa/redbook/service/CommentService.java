package com.chuwa.redbook.service;

import com.chuwa.redbook.payload.Comment.CommentDto;
import com.chuwa.redbook.payload.Comment.CreateCommentDto;

import java.security.Principal;
import java.util.List;

/**
 * @author b1go
 * @date 6/23/22 11:13 PM
 */
public interface CommentService {

    CreateCommentDto createComment(long postId, CreateCommentDto createCommentDto, Principal principal);

    List<CommentDto> getCommentsByPostId(long postId);

    List<CommentDto> getCommentsByUserId(long userId);

    CommentDto getCommentById(Long postId, Long commentId);

    CreateCommentDto updateComment(Long postId, Long commentId, CreateCommentDto createCommentDto, Principal principal);

    void deleteComment(Long postId, Long commentId, Principal principal);

}
