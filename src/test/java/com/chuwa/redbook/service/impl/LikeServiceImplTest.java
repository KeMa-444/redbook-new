package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.LikeRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.security.UserRepository;
import com.chuwa.redbook.entity.Like;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.security.User;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.payload.Like.LikeDto;
import com.chuwa.redbook.payload.Like.LikeResponseDto;
import com.chuwa.redbook.payload.Like.LikeResponseDto_ByUser;
import com.chuwa.redbook.payload.Like.UnLikeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author derri on 11/27/2022
 */
@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImplTest.class);

    @Mock
    private LikeRepository likeRepositoryMock;
    @Mock
    private PostRepository postRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private LikeServiceImpl likeService;

    private Post post;

    private User user;

    private Like like;

    private LikeDto likeDto;
    private Like unlike;
    private UnLikeDto unLikeDto;

    private LikeResponseDto likeResponseDto;

    private LikeResponseDto_ByUser likeResponseDto_byUser;

    private Principal principal;

    @BeforeAll
    static void beforeAll() { logger.info("START test");}
    @BeforeEach
    void setup() {
        logger.info("setting up for each test");
        ModelMapper modelMapper = new ModelMapper();
        this.user = new User(1L, "test", "test", "test@gmail.com", "test");
        this.like = new Like(1L, 1L, 1L, 1L);
        this.unlike = new Like(1L, 0L, 1L, 1L);
        this.unLikeDto = modelMapper.map(this.unlike, UnLikeDto.class);
        this.likeDto = modelMapper.map(this.like, LikeDto.class);
        this.principal = new Principal() {
            @Override
            public String getName() {
                return "test@gmail.com";
            }
        };
        this.post = new Post(1L, "test title", "test description", "test content",
                LocalDateTime.now(), LocalDateTime.now());
    }
    @Test
    void createLike() {
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepositoryMock.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(user));
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));
        Mockito.when(likeRepositoryMock.save(ArgumentMatchers.any(Like.class)))
                .thenReturn(like);

        LikeDto likeResponse = likeService.createLike(likeDto, principal);

        Assertions.assertNotNull(likeResponse);
        Assertions.assertEquals(likeDto.getStatus(), likeResponse.getStatus());
        Assertions.assertEquals(likeDto.getPost(), likeResponse.getPost());
        Assertions.assertEquals(likeDto.getUser(),likeResponse.getUser());
    }

    @Test
    void createLike_Exception() {
        User user1 = new User(2L, "test1", "test1", "test1@gmail.com", "test1");
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepositoryMock.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(user1));

        Exception exception = assertThrows(BlogAPIException.class, () -> {
            likeService.createLike(likeDto, principal);
        });

        Assertions.assertEquals("Provided User ID Invalid", exception.getMessage());

    }

    @Test
    void getLikesByPostId() {
        List<Object> users = new ArrayList<>();
        users.add(user);
        List<Like> likes = new ArrayList<>();
        likes.add(like);
        LikeResponseDto likeResponseDto = new LikeResponseDto(1, users, likes);
        long count = 1;
        Mockito.when(likeRepositoryMock.countByPost(ArgumentMatchers.anyLong()))
                .thenReturn(count);
        Mockito.when(likeRepositoryMock.findByPost(ArgumentMatchers.anyLong()))
                .thenReturn(likes);
        Mockito.when(likeRepositoryMock.getUserByPost(ArgumentMatchers.anyLong()))
                .thenReturn(users);

        LikeResponseDto likeResponse = likeService.getLikesByPostId(1L);


        Assertions.assertNotNull(likeResponse);
        Assertions.assertEquals(count, likeResponse.getTotal());
        Assertions.assertEquals(likes, likeResponse.getDetails());
        Assertions.assertEquals(users, likeResponse.getUsers());
    }

    @Test
    void unlikePost() {
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepositoryMock.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(user));
        Mockito.when(likeRepositoryMock.existsByPostAndUser(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(true);
        Mockito.doNothing().when(likeRepositoryMock).deleteByPostAndUser(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        likeService.unlikePost(unLikeDto, principal);

        Mockito.verify(likeRepositoryMock, Mockito.times(1)).deleteByPostAndUser(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

    }

    @Test
    void unlikePost_Exception_1() {
        User user1 = new User(2L, "test1", "test1", "test1@gmail.com", "test1");
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepositoryMock.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(user1));

        Exception exception = assertThrows(BlogAPIException.class, () -> {
            likeService.unlikePost(unLikeDto, principal);
        });

        Assertions.assertEquals("Provided User ID Invalid", exception.getMessage());

    }

    @Test
    void unlikePost_Exception_2() {
        Mockito.when(likeRepositoryMock.existsByPostAndUser(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(false);

        Exception exception = assertThrows(BlogAPIException.class, () -> {
            likeService.unlikePost(unLikeDto, principal);
        });

        Assertions.assertEquals("The Post is NOT Liked Before", exception.getMessage());

    }

    @Test
    void getLikesByUserId() {
        List<Object> posts = new ArrayList<>();
        posts.add(post);
        List<Like> likes = new ArrayList<>();
        likes.add(like);
        LikeResponseDto_ByUser likeResponseDto_byUser = new LikeResponseDto_ByUser(1, posts, likes);
        long count = 1;
        Mockito.when(likeRepositoryMock.countByUser(ArgumentMatchers.anyLong()))
                .thenReturn(count);
        Mockito.when(likeRepositoryMock.findByUser(ArgumentMatchers.anyLong()))
                .thenReturn(likes);
        Mockito.when(likeRepositoryMock.getPostByUser(ArgumentMatchers.anyLong()))
                .thenReturn(posts);

        LikeResponseDto_ByUser likeResponse = likeService.getLikesByUserId(1L);


        Assertions.assertNotNull(likeResponse);
        Assertions.assertEquals(count, likeResponse.getTotal());
        Assertions.assertEquals(likes, likeResponse.getDetails());
        Assertions.assertEquals(posts, likeResponse.getPosts());
    }
}