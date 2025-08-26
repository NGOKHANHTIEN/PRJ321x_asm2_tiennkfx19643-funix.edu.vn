package com.example.asm2.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ApplicationId implements Serializable {
    private Integer postId;
    private Integer userId;

    public ApplicationId() {}

    public ApplicationId(Integer postId, Integer userId) {
        this.postId = postId;
        this.userId = userId;
    }

    // Getters và Setters
    public Integer getPostId() { return postId; }
    public void setPostId(Integer postId) { this.postId = postId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    // Override equals() và hashCode() để JPA sử dụng đúng
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationId that = (ApplicationId) o;
        return Objects.equals(postId, that.postId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}
