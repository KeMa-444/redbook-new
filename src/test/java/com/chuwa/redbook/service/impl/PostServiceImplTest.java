package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.security.UserRepository;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.security.User;
import com.chuwa.redbook.payload.Post.PostDto;
import com.chuwa.redbook.payload.Post.PostResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author derri on 11/26/2022
 */
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImplTest.class);
    @Mock
    private PostRepository postRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;

    /**
     * 未定义的方法（behavior），则调用真实的方法。
     * 已定义的方法when().thenReturn(), 则调用mock的虚假behavior。
     */
    @Spy
    private ModelMapper modelMapper;

    /**
     * 为该class依赖的变量，注入对应的Mock对象。
     * 比如PostServiceImpl依赖postRepository 和modelMapper,则将上面@Mock和@Spy修饰的注入进来
     */
    @InjectMocks
    private PostServiceImpl postService;

    private PostDto postDto;
    private Post post;

    private User user;

    private Principal principal;

    @BeforeAll
    static void beforeAll() {
        logger.info("START test");
    }

    @BeforeEach
    void setUp() {
        logger.info("setting up for each test");

        this.post = new Post(1L, "test title", "test description", "test content",
                LocalDateTime.now(), LocalDateTime.now());
        ModelMapper modelMapper = new ModelMapper();
        this.postDto = modelMapper.map(this.post, PostDto.class);

        this.user = new User(1L, "test", "test", "test@gmail.com", "test");

        this.principal = new Principal() {
            @Override
            public String getName() {
                return "test@gmail.com";
            }
        };


    }

    @Test
    void createPost() {
        Mockito.when(userRepositoryMock.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(user));
        post.setUser(user);
        Mockito.when(postRepositoryMock.save(ArgumentMatchers.any(Post.class)))
                .thenReturn(post);

        // execute
        PostDto postResponse = postService.createPost(postDto, principal);

        // assertions
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(user, post.getUser());
        Assertions.assertEquals(user.getEmail(), principal.getName());
        Assertions.assertEquals(postDto.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(postDto.getDescription(), postResponse.getDescription());
        Assertions.assertEquals(postDto.getContent(), postResponse.getContent());

    }

    @Test
    void getAllPost_title() {
        int pageNo = 1;
        int pageSize = 8;
        String sortBy = "title";
        String sortDir = Sort.Direction.ASC.name();
        Long totalElements = 22L;
        int totalPages = (int) Math.ceil(totalElements/pageSize);
        boolean isLast = pageNo == totalPages;

        List<Post> posts = new ArrayList<>();
        posts.add(post);

        // define the behaviors
        Page<Post> pagePosts = Mockito.mock(Page.class);
        Mockito.when(pagePosts.getContent()).thenReturn(posts);
        Mockito.when(pagePosts.getNumber()).thenReturn(pageNo);
        Mockito.when(pagePosts.getSize()).thenReturn(pageSize);
        Mockito.when(pagePosts.getTotalElements()).thenReturn(totalElements);
        Mockito.when(pagePosts.getTotalPages()).thenReturn(totalPages);
        Mockito.when(pagePosts.isLast()).thenReturn(isLast);

        Mockito.when(postRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(pagePosts);

        // execute
        PostResponse postResponse = postService.getAllPost(pageNo, pageSize, sortBy, sortDir);

        // verify
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(pageNo, postResponse.getPageNo());
        Assertions.assertEquals(pageSize, postResponse.getPageSize());
        Assertions.assertEquals(totalElements, postResponse.getTotalElements());
        Assertions.assertEquals(totalPages, postResponse.getTotalPages());
        Assertions.assertEquals(isLast, postResponse.isLast());

        List<PostDto> content = postResponse.getContent();
        Assertions.assertNotNull(content);
        Assertions.assertEquals(posts.size(), content.size());
        PostDto dto = content.get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(postDto.getTitle(), dto.getTitle());
        Assertions.assertEquals(postDto.getDescription(), dto.getDescription());
        Assertions.assertEquals(postDto.getContent(), dto.getContent());
    }

    @Test
    void getAllPost_title_DESC() {
        int pageNo = 1;
        int pageSize = 8;
        String sortBy = "title";
        String sortDir = Sort.Direction.DESC.name();
        Long totalElements = 22L;
        int totalPages = (int) Math.ceil(totalElements/pageSize);
        boolean isLast = pageNo == totalPages;

        List<Post> posts = new ArrayList<>();
        posts.add(post);

        // define the behaviors
        Page<Post> pagePosts = Mockito.mock(Page.class);
        Mockito.when(pagePosts.getContent()).thenReturn(posts);
        Mockito.when(pagePosts.getNumber()).thenReturn(pageNo);
        Mockito.when(pagePosts.getSize()).thenReturn(pageSize);
        Mockito.when(pagePosts.getTotalElements()).thenReturn(totalElements);
        Mockito.when(pagePosts.getTotalPages()).thenReturn(totalPages);
        Mockito.when(pagePosts.isLast()).thenReturn(isLast);

        Mockito.when(postRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(pagePosts);

        // execute
        PostResponse postResponse = postService.getAllPost(pageNo, pageSize, sortBy, sortDir);

        // verify
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(pageNo, postResponse.getPageNo());
        Assertions.assertEquals(pageSize, postResponse.getPageSize());
        Assertions.assertEquals(totalElements, postResponse.getTotalElements());
        Assertions.assertEquals(totalPages, postResponse.getTotalPages());
        Assertions.assertEquals(isLast, postResponse.isLast());

        List<PostDto> content = postResponse.getContent();
        Assertions.assertNotNull(content);
        Assertions.assertEquals(posts.size(), content.size());
        PostDto dto = content.get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(postDto.getTitle(), dto.getTitle());
        Assertions.assertEquals(postDto.getDescription(), dto.getDescription());
        Assertions.assertEquals(postDto.getContent(), dto.getContent());
    }
}
