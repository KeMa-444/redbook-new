package com.chuwa.redbook.payload.Post;

/**
 * @author b1go
 * @date 8/22/22 6:52 PM
 */
public class PostAbstractDto {
    private Long id;
    /**
     * 1. title should not be null or empty
     * 2. title should have at least 2 characters
     * Question, our database have set it as nullable=false,
     * why do we need to set validation here? what is the benefits?
     */



    public PostAbstractDto() {
    }

    public PostAbstractDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
