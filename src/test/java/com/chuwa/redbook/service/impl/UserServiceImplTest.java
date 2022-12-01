package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.security.UserRepository;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.security.User;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.payload.Post.PostDto;
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
 * @author derri on 11/28/2022
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImplTest.class);

    @Mock
    private PostRepository postRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private Post post;

    private PostDto postDto;

    private Principal principal;

    @BeforeAll
    static void beforeAll() { logger.info("START test");}
    @BeforeEach
    void setup() {
        logger.info("setting up for each test");
        ModelMapper modelMapper = new ModelMapper();

        this.user = new User(1L, "test", "test", "test@gmail.com", "test");
        this.post = new Post(1L, "test", "test", "test",
                LocalDateTime.now(), LocalDateTime.now());
        this.post.setUser(user);
        this.postDto = modelMapper.map(this.post, PostDto.class);
        this.principal = new Principal() {
            @Override
            public String getName() {
                return "test@gmail.com";
            }
        };
    }
    @Test
    void getPostsByUserId() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        Mockito.when(postRepositoryMock.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(posts);

        List<PostDto> postResponse = userService.getPostsByUserId(1L);

        // assertions
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(1, postResponse.size());
        PostDto specific = postResponse.get(0);
        Assertions.assertEquals(postDto.getTitle(), specific.getTitle());
        Assertions.assertEquals(postDto.getDescription(), specific.getDescription());
        Assertions.assertEquals(postDto.getContent(), specific.getContent());
    }

    @Test
    void getPostByPostId() {
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));

        PostDto postResponse = userService.getPostByPostId(1L, 1L);
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());
    }

    @Test
    void getPostByPostId_Exception() {
        User user1 = new User(2L, "test1", "test1", "test1@gmail.com", "test1");
        post.setUser(user1);
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));

        Exception exception = assertThrows(BlogAPIException.class, () -> {
            userService.getPostByPostId(user.getId(), post.getId());
        });

        Assertions.assertEquals("Post does not belong to user", exception.getMessage());

    }

    @Test
    void updatePost() {
        Post updatedPost = new Post();
        String title = "UPDATED - " + post.getTitle();
        postDto.setTitle(title);
        updatedPost.setId(post.getId());
        updatedPost.setTitle(title);
        updatedPost.setContent(post.getContent());
        updatedPost.setDescription(post.getDescription());
        updatedPost.setUser(post.getUser());

        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));
        Mockito.when(postRepositoryMock.save(ArgumentMatchers.any(Post.class)))
                .thenReturn(updatedPost);

        PostDto postResponse = userService.updatePost(1L, 1L, postDto, principal);

        // assertions
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());
        Assertions.assertEquals(postDto.getUser().getId(), postResponse.getUser().getId());

    }

    @Test
    void updatePost_Exception_1() {
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Principal newPrincipal = new Principal() {
            @Override
            public String getName() {
                return "new name";
            }
        };
        Exception exception = assertThrows(BlogAPIException.class, () -> {
            userService.updatePost(1L, 1L, postDto, newPrincipal);
        });

        Assertions.assertEquals("No access to others' posts", exception.getMessage());
    }

    @Test
    void updatePost_Exception_2() {
        User user1 = new User(2L, "test1", "test1", "test1@gmail.com", "test1");
        post.setUser(user1);
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));

        Exception exception = assertThrows(BlogAPIException.class, () -> {
            userService.updatePost(1L, 1L, postDto, principal);
        });

        Assertions.assertEquals("Post does not belong to user", exception.getMessage());

    }

    @Test
    void deletePost() {
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));
        Mockito.doNothing().when(postRepositoryMock).delete(ArgumentMatchers.any(Post.class));

        userService.deletePost(1L, 1L, principal);

        // verify
        // 验证 postRepositoryMock.delete() 被执行过一次
        Mockito.verify(postRepositoryMock, Mockito.times(1)).delete(ArgumentMatchers.any(Post.class));
    }

    @Test
    void deletePost_Exception_1() {
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Principal newPrincipal = new Principal() {
            @Override
            public String getName() {
                return "new name";
            }
        };
        Exception exception = assertThrows(BlogAPIException.class, () -> {
            userService.deletePost(1L, 1L, newPrincipal);
        });

        Assertions.assertEquals("No access to others' posts", exception.getMessage());
    }

    @Test
    void deletePost_Exception_2() {
        User user1 = new User(2L, "test1", "test1", "test1@gmail.com", "test1");
        post.setUser(user1);
        Mockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(postRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(post));

        Exception exception = assertThrows(BlogAPIException.class, () -> {
            userService.deletePost(1L, 1L, principal);
        });

        Assertions.assertEquals("Post does not belong to user", exception.getMessage());

    }
}