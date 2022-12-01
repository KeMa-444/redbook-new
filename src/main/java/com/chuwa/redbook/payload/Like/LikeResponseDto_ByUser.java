package com.chuwa.redbook.payload.Like;

import com.chuwa.redbook.entity.Like;

import java.util.List;


public class LikeResponseDto_ByUser {

    private long total;

    private List<Object> posts;

    private List<Like> details;


    public LikeResponseDto_ByUser() {
    }

    public LikeResponseDto_ByUser(long total, List<Object> posts, List<Like> details) {
        this.total = total;
        this.posts = posts;
        this.details = details;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Like> getDetails() {
        return details;
    }

    public void setDetails(List<Like> details) {
        this.details = details;
    }

    public List<Object> getPosts() {
        return posts;
    }

    public void setPosts(List<Object> posts) {
        this.posts = posts;
    }
}
