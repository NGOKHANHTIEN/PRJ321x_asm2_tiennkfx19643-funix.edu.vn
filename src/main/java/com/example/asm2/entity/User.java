package com.example.asm2.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name="phonenumber")
    private String phoneNum;
    @Column(name="password")
    private String password;
    @Column(name="enable")
    private int enable =1;
    @Column(name="image")
    private String image;
    @Column(name="description")
    private String description;

    @Column(name="role_id")
    private int roleId;

    @OneToOne
    @JoinColumn(name="company_id")
    private Company company;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="cv_id")
    private CV cv;

    @ManyToMany
    @JoinTable(name="followed_post",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> listPostFollow = new ArrayList<>()  ;
    @ManyToMany
    @JoinTable(name="followed_company",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    private List<Company> listCompanyFollow = new ArrayList<>()  ;
    public User() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password =  password;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }



    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }


    public List<Post> getListPostFollow() {
        return listPostFollow;
    }

    public void setListPostFollow(List<Post> listPostFollow) {
        this.listPostFollow = listPostFollow;
    }

    public void addPostFollow(Post post) {
        if (!listPostFollow.contains(post)) {
            listPostFollow.add(post);
        }
    }

    public void removePostFollow(Post post) {
        listPostFollow.remove(post);
    }

    public boolean isFollowing(Post post) {
        return listPostFollow.contains(post);
    }

    public List<Company> getListCompanyFollow() {
        return listCompanyFollow;
    }

    public void setListCompanyFollow(List<Company> listCompanyFollow) {
        this.listCompanyFollow = listCompanyFollow;
    }
    public void addCompanyFollow (Company company){
        if(!listCompanyFollow.contains(company)){
            listCompanyFollow.add(company);
        }
    }
    public void removeCompanyFollow (Company company){
        listCompanyFollow.remove(company);
    }
    public boolean isFollowing(Company company){
        return listCompanyFollow.contains(company);
    }
}
