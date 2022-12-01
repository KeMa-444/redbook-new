package com.chuwa.redbook.payload.Comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


public class CreateCommentDto {

    private long id;


    @NotEmpty
    @Size(min = 5, message = "Comment body must be minimum 5 characters")
    private String body;

    public CreateCommentDto() {
    }

    public CreateCommentDto(String body) {
        this.body = body;
    }

    public CreateCommentDto(long id, String body) {
        this(body);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
