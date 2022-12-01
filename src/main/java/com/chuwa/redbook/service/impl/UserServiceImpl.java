package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.security.UserRepository;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.security.User;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.Post.PostDto;
import com.chuwa.redbook.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author b1go
 * @date 8/22/22 6:56 PM
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    /**
     * use this modelMapper to replace the mapToDto, mapToEntity methods.
     */
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<PostDto> getPostsByUserId(long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostByPostId(long userId, long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        if (!post.getUser().getId().equals(user.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Post does not belong to user");
        }
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto updatePost(long userId, long postId, PostDto postDto, Principal principal) {
        if (!principal.getName().equals(userRepository.findById(userId).get().getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "No access to others' posts");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        if (!post.getUser().getId().equals(user.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Post does not belong to user");
        }
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return modelMapper.map(savedPost, PostDto.class);

    }

    @Override
    public void deletePost(long userId, long postId, Principal principal) {
        if (!principal.getName().equals(userRepository.findById(userId).get().getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "No access to others' posts");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        if (!post.getUser().getId().equals(user.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Post does not belong to user");
        }
        postRepository.delete(post);
    }


}
