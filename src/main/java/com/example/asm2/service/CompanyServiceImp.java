package com.example.asm2.service;

import com.example.asm2.dao.CompanyReposiory;
import com.example.asm2.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImp implements CompanyService{
    private CompanyReposiory companyReposiory;
    public CompanyServiceImp(CompanyReposiory theCompanyRep){
        companyReposiory=theCompanyRep;
    }

    @Override
    public List<Company> findAll() {
        return companyReposiory.findAll();
    }

    @Override
    public Company findById(int id) {
        return companyReposiory.findById(id);
    }



    @Override
    public Company add(Company company){
        return companyReposiory.save(company);
    }

    @Override
    public List<Company> topCompany() {
      return  companyReposiory.topCompany();
    }

    @Override
    public List<Company> findByName(String name){
        return companyReposiory.findByNameContainingIgnoreCase(name);
    }


}
