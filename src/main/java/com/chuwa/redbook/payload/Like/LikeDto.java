package com.chuwa.redbook.payload.Like;

import javax.validation.constraints.*;


public class LikeDto {


    private long post;

    private long user;

    @NotNull
    @Min(value = 1, message = "Please Enter 1 for Like")
    @Max(value = 1,message = "Please Enter 1 for Like")
    private long status;

    public LikeDto() {
    }

    public long getPost() {
        return post;
    }

    public void setPost(long post) {
        this.post = post;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}
