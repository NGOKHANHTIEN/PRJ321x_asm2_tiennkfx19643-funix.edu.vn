package com.example.asm2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="address")
    private String address;
    @Column(name="email")
    private String email;
    @Column(name="phonenumber")
    private String phoneNum;
    @Column(name="logo")
    private String logo;
    @Column(name="description")
    private String description;
    @OneToOne(mappedBy = "company")
    private User user;
    @OneToMany(mappedBy = "company")
    private List<Post> postList;


    public Company() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        user.setCompany(this);
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        for(Post post:postList){
            post.setCompany(this);
        }
    }
}
