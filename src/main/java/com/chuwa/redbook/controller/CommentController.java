package com.chuwa.redbook.controller;

import com.chuwa.redbook.payload.Comment.CommentDto;
import com.chuwa.redbook.payload.Comment.CreateCommentDto;
import com.chuwa.redbook.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author b1go
 * @date 6/23/22 11:30 PM
 */
@RestController
@RequestMapping("/api/v1")
public class CommentController {

    /**
     * TODO: Questions
     * why intellij give us this warning? constructor injection.
     * how many ways we can do Dependency Injection?
     * which way is the best one?
     */
    @Autowired
    private CommentService commentService;

    /**
     * TODO: Questions
     * 当我们浏览小红书时候，点开一篇文章，请问获得这篇文章的内容，是用的哪个API？
     * 看到大家争论库里历史地位是否超越科比，你要写评论回应，当你的评论提交时候，会call哪个API？
     * <p>
     * 此时此刻，思考为什么post的ID是pathVariable 而不是request parameter?
     *
     * @param
     * @param
     * @return
     */
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CreateCommentDto> createComment(@PathVariable(value = "postId") long id,
                                                    @Valid @RequestBody CreateCommentDto createCommentDto,
                                                    Principal principal) {
        return new ResponseEntity<>(commentService.createComment(id, createCommentDto, principal), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") Long postId) {
        return commentService.getCommentsByPostId(postId);
    }



    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentsById(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "id") Long commentId) {

        CommentDto commentDto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CreateCommentDto> updateComment(@PathVariable(value = "postId") Long postId,
                                                          @PathVariable(value = "commentId") Long commentId,
                                                          @Valid @RequestBody CreateCommentDto createCommentDto,
                                                          Principal principal) {
        CreateCommentDto updateComment = commentService.updateComment(postId, commentId, createCommentDto, principal);
        return new ResponseEntity<>(updateComment, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "id") Long commentId,
                                                Principal principal) {
        commentService.deleteComment(postId, commentId, principal);

        return new ResponseEntity<>("Comment deleted Successfully", HttpStatus.OK);
    }
}
