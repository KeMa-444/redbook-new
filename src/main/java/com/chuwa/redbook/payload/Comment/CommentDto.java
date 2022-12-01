package com.chuwa.redbook.payload.Comment;

import com.chuwa.redbook.payload.User.UserAbstractDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


public class CommentDto {

    private long id;

    private UserAbstractDto user;
    @NotEmpty
    @Size(min = 5, message = "Comment body must be minimum 5 characters")
    private String body;

    public CommentDto() {
    }

    public CommentDto(UserAbstractDto user, String body) {
        this.user = user;
        this.body = body;
    }

    public CommentDto(long id, UserAbstractDto user, String body) {
        this(user, body);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserAbstractDto getUser() {
        return user;
    }

    public void setUser(UserAbstractDto user) {
        this.user = user;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
