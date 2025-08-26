package com.example.asm2.dao;

import com.example.asm2.entity.Application;
import com.example.asm2.entity.ApplicationId;
import com.example.asm2.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, ApplicationId> {
    @Query (value = "select * from applications a  join posts p on a.post_id=p.id where p.company_id=:companyID",nativeQuery = true)
    List<Application> listApplicationOfCompany(@Param("companyID") int companyID);
    List<Application> findByUser_Id (int userid);
    @Query("SELECT a.post FROM Application a WHERE a.user.id = :userId")
    List<Post> findPostsAppliedByUser(@Param("userId") int userId);

}
