package com.example.asm2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cv")
public class CV {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "file_name")
    private String fileName;
    @OneToOne(mappedBy = "cv")
    private User user;

    public CV() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.setCv(this);
    }


}
