package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.LikeRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.security.UserRepository;
import com.chuwa.redbook.entity.Like;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.security.User;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.Like.LikeDto;
import com.chuwa.redbook.payload.Like.LikeResponseDto;
import com.chuwa.redbook.payload.Like.LikeResponseDto_ByUser;
import com.chuwa.redbook.payload.Like.UnLikeDto;
import com.chuwa.redbook.service.LikeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LikeDto createLike(LikeDto likeDto, Principal principal) {
        Like like = modelMapper.map(likeDto, Like.class);
        long userId = like.getUser();

        if (!userRepository.findById(userId).equals(userRepository.findByEmail(principal.getName()))) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Provided User ID Invalid");
        }

        long postId = like.getPost();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        like.setPost(post.getId());

        User user = userRepository.findByEmail(principal.getName())
                  .orElseThrow(() -> new BlogAPIException(HttpStatus.BAD_REQUEST, "User No Found"));
        like.setUser(user.getId());

        Like savedLike = likeRepository.save(like);
        return modelMapper.map(savedLike, LikeDto.class);
    }

    @Override
    public LikeResponseDto getLikesByPostId(long postId) {
        LikeResponseDto likeResponseDto = new LikeResponseDto();
        long count = likeRepository.countByPost(postId);
        List<Like> likes = likeRepository.findByPost(postId);
        List<Object> users = likeRepository.getUserByPost(postId);

        likeResponseDto.setDetails(likes);
        likeResponseDto.setTotal(count);
        likeResponseDto.setUsers(users);

        return likeResponseDto;
    }

    @Override
    public void unlikePost(UnLikeDto unLikeDto, Principal principal) {
        Like like = modelMapper.map(unLikeDto, Like.class);
        long userId = like.getUser();

        if (!userRepository.findById(userId).equals(userRepository.findByEmail(principal.getName()))) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Provided User ID Invalid");
        }

        long postId = like.getPost();
        if (!likeRepository.existsByPostAndUser(postId, userId))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "The Post is NOT Liked Before");


        likeRepository.deleteByPostAndUser(postId,userId);
    }

    @Override
    public LikeResponseDto_ByUser getLikesByUserId(long userId) {
        LikeResponseDto_ByUser likeResponseDto_byUser = new LikeResponseDto_ByUser();
        long count = likeRepository.countByUser(userId);
        List<Like> likes = likeRepository.findByUser(userId);
        List<Object> posts = likeRepository.getPostByUser(userId);

        likeResponseDto_byUser.setDetails(likes);
        likeResponseDto_byUser.setTotal(count);
        likeResponseDto_byUser.setPosts(posts);

        return likeResponseDto_byUser;
    }

}
