package com.chuwa.redbook.payload.User;

import com.chuwa.redbook.payload.Post.PostDto;

import java.util.Set;

/**
 * @author b1go
 * @date 8/22/22 6:52 PM
 */
public class UserDto {
    private Long id;
    /**
     * 1. title should not be null or empty
     * 2. title should have at least 2 characters
     * Question, our database have set it as nullable=false,
     * why do we need to set validation here? what is the benefits?
     */

    private Set<PostDto> posts;

    public UserDto() {
    }

    public UserDto(Long id, Set<PostDto> posts) {
        this.id = id;
        this.posts = posts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(Set<PostDto> posts) {
        this.posts = posts;
    }
}
