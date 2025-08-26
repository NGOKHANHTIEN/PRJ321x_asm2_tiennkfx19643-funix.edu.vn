package com.example.asm2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applications")
public class Application{
    @EmbeddedId
    private ApplicationId id;
    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "is_approved")
    private boolean isApproved;

    public Application() {
    }

    public Application(Post post, User user) {
        this.id = new ApplicationId(post.getId(), user.getId());
        this.post = post;
        this.user = user;

    }

    public ApplicationId getId() { return id; }
    public void setId(ApplicationId id) { this.id = id; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }


    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
