package com.example.asm2.service;

import com.example.asm2.entity.Company;

import java.util.List;

public interface CompanyService  {
    List<Company> findAll ();
    Company findById (int id);

    Company add(Company company);
    List<Company> topCompany();


    List<Company> findByName(String name);
}
