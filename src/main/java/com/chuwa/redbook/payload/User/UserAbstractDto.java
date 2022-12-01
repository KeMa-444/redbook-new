package com.chuwa.redbook.payload.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author b1go
 * @date 8/22/22 6:52 PM
 */
public class UserAbstractDto {
    private Long id;
    /**
     * 1. title should not be null or empty
     * 2. title should have at least 2 characters
     * Question, our database have set it as nullable=false,
     * why do we need to set validation here? what is the benefits?
     */



    public UserAbstractDto() {
    }

    public UserAbstractDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
