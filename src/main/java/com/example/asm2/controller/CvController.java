package com.example.asm2.controller;

import com.example.asm2.dao.ApplicationRepository;
import com.example.asm2.entity.Application;
import com.example.asm2.entity.ApplicationId;
import com.example.asm2.entity.User;
import com.example.asm2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cv")
public class CvController {
    private ApplicationRepository applicationRepository;
    private UserService userService;
    public CvController( UserService userService1, ApplicationRepository applicationRepository1) {
        this.applicationRepository=applicationRepository1;
        this.userService=userService1;
    }
    @RequestMapping("/list")
    public  String listCVOfCompany (@RequestParam(value = "page", defaultValue = "1")int page, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if(auth.getPrincipal() instanceof UserDetails userDetails){
            String email = userDetails.getUsername();
            currentUser = userService.findByEmail(email);
        }
        int companyId =currentUser.getCompany().getId();
        List<Application> list = applicationRepository.listApplicationOfCompany(companyId);
        int start =(int)PageRequest.of(page-1,5).getOffset();
        int end = Math.min(start +5,list.size());
        List<Application> sublist = list.subList(start,end);
        Page<Application> applicationPage = new PageImpl<>(sublist,PageRequest.of(page-1,5),list.size()) ;

        model.addAttribute("applicationList",applicationPage.getContent());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",applicationPage.getTotalPages());
        return "cv_list";
    }
    @RequestMapping("/approve")
    public String approveCV (@RequestParam("postId") Integer postid, @RequestParam("userId") Integer userid , Model model, HttpServletRequest request){
        ApplicationId applicationId = new ApplicationId(postid,userid);
       Optional<Application> applicationApproved = applicationRepository.findById(applicationId);
       Application application = null;
       if(applicationApproved.isPresent()){
           application = applicationApproved.get();
       }
       application.setApproved(true);
       applicationRepository.save(application);
        String referer = request.getHeader("Referer");
        return "redirect:"+(referer != null ? referer : "/home");
    }

    @RequestMapping("/list-post-applied")
    public String listPostApplied (@RequestParam(value = "page", defaultValue = "1")int page, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if(auth.getPrincipal() instanceof UserDetails userDetails){
            String  email = userDetails.getUsername();
            currentUser = userService.findByEmail(email);

        }
        List<Application> applicationList = applicationRepository.findByUser_Id(currentUser.getId());
        int start = (int) PageRequest.of(page -1,5).getOffset();
        int end = Math.min(start+5,applicationList.size());
        List<Application> subList = applicationList.subList(start,end);
        Page<Application> postPage = new PageImpl<>(subList,PageRequest.of(page-1,5),applicationList.size());
        List<Application> applicationList1 = postPage.getContent();

        model.addAttribute("applicationList",applicationList1);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",postPage.getTotalPages());
        return "post_applied_list";
    }
}
