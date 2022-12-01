package com.chuwa.redbook.payload.Like;

import javax.validation.constraints.*;


public class UnLikeDto {


    private long post;

    private long user;

    @NotNull
    @Min(value = 0, message = "Please Enter 0 for Unlike")
    @Max(value = 0,message = "Please Enter 0 for Unlike")
    private long status;


    public UnLikeDto() {
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
