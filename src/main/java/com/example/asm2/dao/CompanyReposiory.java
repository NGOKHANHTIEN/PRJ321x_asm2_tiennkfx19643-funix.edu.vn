package com.example.asm2.dao;

import com.example.asm2.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyReposiory extends JpaRepository<Company,Integer> {
    @Query(value = "SELECT c.* FROM company c " +
            "JOIN posts p ON c.id = p.company_id " +
            "GROUP BY c.id, c.name, c.address, c.email, c.phonenumber, c.logo, c.description " +
            "ORDER BY COUNT(p.id) DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Company> topCompany ();

    Company findById(int id);
    List<Company> findByNameContainingIgnoreCase (String name);
}
