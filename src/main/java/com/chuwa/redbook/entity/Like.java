package com.chuwa.redbook.entity;

import javax.persistence.*;


@Entity
@Table(name = "likes",
        uniqueConstraints = { @UniqueConstraint(columnNames =
                { "post", "user" }) })

public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long status;

    @Column(name = "post", nullable = false)
    private long post;

    @Column(name = "user", nullable = false)
    private long user;



    public Like() {
    }

    public Like(long id, long status, long post, long user) {
        this.id = id;
        this.status = status;
        this.post = post;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
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
}
