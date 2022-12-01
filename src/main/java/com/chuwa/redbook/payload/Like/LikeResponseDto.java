package com.chuwa.redbook.payload.Like;

import com.chuwa.redbook.entity.Like;

import java.util.List;


public class LikeResponseDto {

    private long total;

    private List<Object> users;

    private List<Like> details;


    public LikeResponseDto() {
    }

    public LikeResponseDto(long total, List<Object> users, List<Like> details) {
        this.total = total;
        this.users = users;
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

    public List<Object> getUsers() {
        return users;
    }

    public void setUsers(List<Object> users) {
        this.users = users;
    }
}
