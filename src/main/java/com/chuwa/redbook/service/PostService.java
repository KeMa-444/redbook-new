package com.chuwa.redbook.service;

import com.chuwa.redbook.payload.Post.PostDto;
import com.chuwa.redbook.payload.Post.PostResponse;

import java.security.Principal;

/**
 * @author b1go
 * @date 8/22/22 6:51 PM
 */
public interface PostService {

    PostDto createPost(PostDto postDto, Principal principal);

    /**
     * 分页
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir);


}
