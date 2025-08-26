package com.example.asm2.controller;

import com.example.asm2.dao.ApplicationRepository;
import com.example.asm2.entity.Category;
import com.example.asm2.entity.Company;
import com.example.asm2.entity.Post;
import com.example.asm2.entity.User;
import com.example.asm2.service.CategoryService;
import com.example.asm2.service.CompanyService;
import com.example.asm2.service.PostService;
import com.example.asm2.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private CategoryService categoryService;
    private UserService userService;
    private PostService postService;
    private CompanyService companyService;
    private ApplicationRepository applicationRepository;

    public HomeController(CategoryService categoryService, UserService userService1,
                          PostService postService1, CompanyService companyService1, ApplicationRepository applicationRepository1) {
        this.categoryService = categoryService;
        this.userService=userService1;
        this.postService=postService1;
        this.companyService = companyService1;
        this.applicationRepository = applicationRepository1;
    }
    @GetMapping("/home")
    public String goToHome(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        List<Category> topCategory = categoryService.topCategory();
        model.addAttribute("topCategory",topCategory);
        int applicant_count= userService.findAllApplicant().size();
        model.addAttribute("applicant_count",applicant_count);
        int company_count = companyService.findAll().size();
        model.addAttribute("company_count",company_count);
        int post_count = postService.findAll().size();
        model.addAttribute("post_count",post_count);
        List<Post> topPost = postService.topPostApplied();
        model.addAttribute("topPostList",topPost);
        List<Company> topCompany = companyService.topCompany();
        model.addAttribute("topCompany",topCompany);
        if(existingUser!=null) {
            List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());

            model.addAttribute("appliedPost", listPostApplied);
        }
        return "home";
    }
}
