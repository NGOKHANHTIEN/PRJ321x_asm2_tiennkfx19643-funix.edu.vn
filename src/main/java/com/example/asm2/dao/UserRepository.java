package com.example.asm2.dao;

import com.example.asm2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);
    @Query(value = "select * from user where enable='1' and role_id='1'", nativeQuery = true)
    List<User> listApplicant ();
    User findByEmail(String email);
    @Query(value = "select u.id , u.name, u.email, u.address, u.phonenumber, " +
            "u.password, u.enable, u.image, u.description, u.company_id, u.role_id, u.cv_id from user u " +
            "join applications a on u.id = a.user_id " +
            "join posts p on p.id = a.post_id " +
            "where p.company_id =:companyId ", nativeQuery = true)
    List<User> listUserApplyInCompany(@Param("companyId")int companyId);

}
