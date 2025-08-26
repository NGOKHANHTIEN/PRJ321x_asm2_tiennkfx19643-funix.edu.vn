package com.example.asm2.service;

import com.example.asm2.dao.CVRepository;
import com.example.asm2.dao.UserRepository;
import com.example.asm2.entity.CV;
import com.example.asm2.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService{
    private UserRepository userRepository;
    private CVRepository cvRepository;
    public UserServiceImp(UserRepository theUserRepository, CVRepository cvRepository1){
        userRepository=theUserRepository;
        cvRepository = cvRepository1;
    }
    @Override
    public boolean isUserExist(String email) {

        return userRepository.existsByEmail(email);
    }
    @Override
    public  void addUser(User user){
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAllApplicant() {
        return userRepository.listApplicant();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(CV cv) {
        cvRepository.save(cv);

    }

    @Override
    @Transactional
    public void delete(int id){
        userRepository.deleteById(id);
    }
    @Override
    public List<User> listUserApllyInCompany(int companyId){
        return userRepository.listUserApplyInCompany(companyId);

    }
}
