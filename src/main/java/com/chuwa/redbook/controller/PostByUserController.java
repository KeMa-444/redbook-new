package com.chuwa.redbook.controller;

import com.chuwa.redbook.payload.Comment.CommentDto;
import com.chuwa.redbook.payload.Like.LikeResponseDto_ByUser;
import com.chuwa.redbook.payload.Post.PostDto;
import com.chuwa.redbook.service.CommentService;
import com.chuwa.redbook.service.LikeService;
import com.chuwa.redbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author b1go
 * @date 8/22/22 7:14 PM
 */
@RestController
@RequestMapping("/api/v1/users")
public class PostByUserController {


    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;


    @GetMapping("/{userId}/posts")
    public List<PostDto> getPostsByUserId(@PathVariable(value = "userId") Long userId) {
        return userService.getPostsByUserId(userId);
    }

    @GetMapping("/{userId}/comments")
    public List<CommentDto> getCommentsByUserId(@PathVariable(value = "userId") Long userId) {
        return commentService.getCommentsByUserId(userId);
    }

    @GetMapping("/{userId}/likes")
    public ResponseEntity<LikeResponseDto_ByUser> getLikesByUserId(@PathVariable(value = "userId") Long userId) {
        return new ResponseEntity<>(likeService.getLikesByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(value = "userId") Long userId,
                                               @PathVariable(value = "postId") Long postId) {

        return new ResponseEntity<>(userService.getPostByPostId(userId, postId), HttpStatus.OK);
    }

    @PutMapping("/{userId}/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(value = "userId") Long userId,
                                              @PathVariable(value = "postId") Long postId,
                                              @Valid @RequestBody PostDto postDto,
                                              Principal principal) {
        PostDto updatePost = userService.updatePost(userId, postId, postDto, principal);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "userId") Long userId,
                                             @PathVariable(value = "postId") Long postId,
                                             Principal principal) {
        userService.deletePost(userId, postId, principal);
        return new ResponseEntity<>("Post deleted Successfully", HttpStatus.OK);
    }


}
