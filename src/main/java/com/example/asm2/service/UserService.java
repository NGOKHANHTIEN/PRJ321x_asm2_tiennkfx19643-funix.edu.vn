package com.example.asm2.service;

import com.example.asm2.entity.CV;
import com.example.asm2.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean isUserExist (String email);
    void addUser(User user);
    Optional<User> findById(int id);
    List<User> findAllApplicant();
    User findByEmail(String email);
    void save (CV sv);

    void delete(int id);

    List<User> listUserApllyInCompany(int companyId);
}
