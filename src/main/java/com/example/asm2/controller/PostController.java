package com.example.asm2.controller;


import com.example.asm2.dao.ApplicationRepository;
import com.example.asm2.entity.*;
import com.example.asm2.service.CategoryService;
import com.example.asm2.service.CompanyService;
import com.example.asm2.service.PostService;
import com.example.asm2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    private PostService postService;
    private CategoryService categoryService;
    private UserService userService;
    private CompanyService companyService;
    private ApplicationRepository applicationRepository;

    public PostController(PostService postService, CategoryService categoryService, UserService userService1,
                          CompanyService companyService1, ApplicationRepository applicationRepository1){
        this.postService = postService;
        this.categoryService= categoryService;
        this.userService = userService1;
        this.companyService =companyService1;
        this.applicationRepository =applicationRepository1;
    }
    @GetMapping("/list")
    public String list( Model model, @RequestParam(value = "page", defaultValue = "1") int page){
        Pageable pageable = PageRequest.of(page-1,5);
        Page<Post> postPage = postService.fetchPost(pageable);

        List<Post> postList = postPage.getContent();
        model.addAttribute("request","/post/list");
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",postPage.getTotalPages());
        model.addAttribute("postList",postList);
        List<Category> topCategory = categoryService.topCategory();
        model.addAttribute("topCategory",topCategory);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        if(existingUser!= null) {
            List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());

            model.addAttribute("appliedPost", listPostApplied);
            model.addAttribute("user", existingUser);
        }
        return "post_list";
    }
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model){
        model.addAttribute("post",new Post());
        model.addAttribute("categoriesList",categoryService.findAll());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        Company company = existingUser.getCompany();
        model.addAttribute("company",company);
        return "post_form";
    }
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("postId") int id, Model model){

         Post thePost = postService.findById(id);

        model.addAttribute("post",thePost);
        model.addAttribute("categoriesList",categoryService.findAll());
        return "post_form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute("post")Post thePost,
                       @RequestParam("categoryIdList") List<Integer> categoryIds){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        Company company = existingUser.getCompany();
        thePost.setCompany(company);
        company.getPostList().add(thePost);
        List<Category> categoryList = categoryService.getCategoryByIds(categoryIds);
        thePost.setCategoryList(categoryList);
        for(Category category : categoryList){
            category.addPost(thePost);
        }



        postService.save(thePost);
        return "redirect:/post/list";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam("postId") int id){

        Post post=postService.findById(id);
        List<Category> categoryList = post.getCategoryList();
        for(Category category : categoryList){
            category.getPostList().remove(post);
        }
        postService.deleteById(id);
        return "redirect:/post/list";
    }
    @GetMapping("/allPostByCategory")
    public String allPostByCategory (@RequestParam("categoryId") int id,@RequestParam(value = "page", defaultValue = "1")int page, Model model){
        Category category = categoryService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }

            List<Post> postList = category.getPostList();
        //phân trang
        int start = (int)PageRequest.of(page-1,5).getOffset();
        int end = Math.min(start+5,postList.size());
        List<Post> sublist = postList.subList(start,end);
        Page<Post> postPage = new PageImpl<>(sublist,PageRequest.of(page -1,5),postList.size());
        List<Post> postList1 = postPage.getContent();

            model.addAttribute("request","/post/allPostByCategory");
            model.addAttribute("categoryId",id);
            model.addAttribute("postList", postList1);
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPage",postPage.getTotalPages());
        if(existingUser!= null) {
            List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());

            model.addAttribute("appliedPost", listPostApplied);
        }
        return "post_list";
    }
    @GetMapping("/allPostByTitle")
    public  String allPostByTitle(@RequestParam("keySearch")String title,@RequestParam(value = "page", defaultValue = "1")int page, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
    List<Post> postList =postService.findByTitle(title);
        //phân trang
        int start = (int)PageRequest.of(page-1,5).getOffset();
        int end = Math.min(start+5,postList.size());
        List<Post> sublist = postList.subList(start,end);
        Page<Post> postPage = new PageImpl<>(sublist,PageRequest.of(page -1,5),postList.size());
        List<Post> postList1 = postPage.getContent();
        model.addAttribute("request","/post/allPostByTitle");
        model.addAttribute("title",title);
        model.addAttribute("postList", postList1);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",postPage.getTotalPages());



    model.addAttribute("postList",postList1);
        if(existingUser!= null) {
            List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());

            model.addAttribute("appliedPost", listPostApplied);
        }
    return "post_list";
    }
    @GetMapping("/allPostByCompanyName")
    public String allPostByCompanyName(@RequestParam ("keySearchCompany") String companyName,@RequestParam(value = "page", defaultValue = "1")int page, Model model){
        List<Company> companyList = companyService.findByName(companyName);
        List<Post> postList = new ArrayList<>();
        for(Company company : companyList){
            postList.addAll(company.getPostList());
        }
        //phân trang
        int start = (int)PageRequest.of(page -1,5).getOffset();
        int end = Math.min(start+5, postList.size());
        List<Post> subList = postList.subList(start,end);
        Page<Post> postPage = new PageImpl<>(subList,PageRequest.of(page -1,5),postList.size());
        List<Post> postList1 = postPage.getContent();
        model.addAttribute("postList",postList1);
        model.addAttribute("request","/post/allPostByCompanyName");
        model.addAttribute("keySearchCompany",companyName);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",postPage.getTotalPages());
        //lấy ds post applied
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        if(existingUser!= null) {
            List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());

            model.addAttribute("appliedPost", listPostApplied);
        }
        return "post_list";

    }
    @GetMapping("/allPostByAddress")
    public  String allPostByAddress(@RequestParam("keySearchAddress")String address,@RequestParam(value = "page", defaultValue = "1")int page, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        List<Post> postList =postService.findByAddress(address);
        //phân trang
        int start = (int)PageRequest.of(page-1,5).getOffset();
        int end = Math.min(start+5,postList.size());
        List<Post> sublist = postList.subList(start,end);
        Page<Post> postPage = new PageImpl<>(sublist,PageRequest.of(page -1,5),postList.size());
        List<Post> postList1 = postPage.getContent();
        model.addAttribute("request","/post/allPostByAddress");
        model.addAttribute("address",address);
        model.addAttribute("postList", postList1);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",postPage.getTotalPages());

        model.addAttribute("postList",postList1);
        if(existingUser!= null) {
            List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());

            model.addAttribute("appliedPost", listPostApplied);
        }
        return "post_list";
    }
    @GetMapping("/applyJob")
    public String applyJob(@RequestParam("postId") int id,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
        Post postToApply = postService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth.getPrincipal() instanceof UserDetails userDetails)) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        User existingUser = userService.findByEmail(email);

        // ✅ Kiểm tra nếu user chưa có CV thì trả về với thông báo
        if (existingUser.getCv() == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần cập nhật CV trước khi ứng tuyển.");
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/home");
        }

        // Nếu đã có CV → tiếp tục apply
        Application application = new Application(postToApply, existingUser);
        application.setApproved(false);
        applicationRepository.save(application);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }


    @GetMapping("/postOfCompany")
    public  String postOfCompany(@RequestParam("companyId") int companyId,@RequestParam(value = "page",defaultValue = "1")int page, Model model){
        Company company = companyService.findById(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        List<Post> postList = company.getPostList();
        //phân trang
        int start = (int)PageRequest.of(page-1,5).getOffset();
        int end = Math.min(start+5,postList.size());
        List<Post> sublist = postList.subList(start,end);
        Page<Post> postPage = new PageImpl<>(sublist,PageRequest.of(page -1,5),postList.size());
        List<Post> postList1 = postPage.getContent();
        model.addAttribute("request","/post/postOfCompany");
        model.addAttribute("companyId",companyId);
        model.addAttribute("postList", postList1);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",postPage.getTotalPages());
        model.addAttribute("postList",postList1);
        if(existingUser!= null) {
            List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());

            model.addAttribute("appliedPost", listPostApplied);
        }
        return  "post_list";
}
@GetMapping("/addPostFollow")
    public String addPostFollow (@RequestParam("postId")int id,  HttpServletRequest request){
        Post post = postService.findById(id);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User existingUser = null;
    if (auth.getPrincipal() instanceof UserDetails userDetails) {
        String email = userDetails.getUsername();

        existingUser = userService.findByEmail(email);
    }
    if (existingUser.isFollowing(post)) {
        existingUser.removePostFollow(post);
    } else {
        existingUser.addPostFollow(post);
    }
    userService.addUser(existingUser);
    String referer = request.getHeader("Referer");
    return "redirect:"+(referer != null ? referer : "/home");
}
@GetMapping("/postFollowed")
    public String listPostFollowed (@RequestParam(value = "page", defaultValue = "1")int page, Model model){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User existingUser = null;
    if (auth.getPrincipal() instanceof UserDetails userDetails) {
        String email = userDetails.getUsername();

        existingUser = userService.findByEmail(email);
    }
    List<Post> listPostFollowed = existingUser.getListPostFollow();

    int start = (int)PageRequest.of(page -1,5).getOffset();
    int end = Math.min(start+5,listPostFollowed.size());
    List<Post> subList = listPostFollowed.subList(start,end);
    Page<Post> postPage = new PageImpl<>(subList,PageRequest.of(page-1,5),listPostFollowed.size());
    List<Post> postList = postPage.getContent();
    List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());

    model.addAttribute("appliedPost",listPostApplied);
    model.addAttribute("postList",postList);
    model.addAttribute("currentPage",page);
    model.addAttribute("totalPage",postPage.getTotalPages());
    return "post_list_followed";
}
@GetMapping("/detail/{id}")
    public String postDetail(@PathVariable("id") int id, Model model){
        Post post = postService.findById(id);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User existingUser = null;
    if (auth.getPrincipal() instanceof UserDetails userDetails) {
        String email = userDetails.getUsername();

        existingUser = userService.findByEmail(email);
    }
        model.addAttribute("post",post);
        model.addAttribute("user",existingUser);
    if(existingUser != null) {
        List<Post> listPostApplied = applicationRepository.findPostsAppliedByUser(existingUser.getId());
        model.addAttribute("appliedPost", listPostApplied);
    }
        return "detail-post";
}
    @GetMapping("/addCompanyFollow")
    public String addCompanyFollow (@RequestParam("companyId")int id, RedirectAttributes redirectAttributes, HttpServletRequest request){
        Company company = companyService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        boolean follow = false;
        if(existingUser.isFollowing(company)){
            existingUser.getListCompanyFollow().remove(company);
        }else {
            existingUser.getListCompanyFollow().add(company);
            follow = true;
        }
        userService.addUser(existingUser);
        if(follow){
            redirectAttributes.addFlashAttribute("followSuccess", true);

        }else {
            redirectAttributes.addFlashAttribute("unfollowSuccess", true);

        }

        String referer = request.getHeader("Referer");
        return "redirect:"+(referer != null ? referer : "/home");
    }

    @GetMapping("/companyFollowed")
    public String listCompanyFollowed (@RequestParam(value = "page" , defaultValue = "1")int page, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        List<Company> listCompanyFollowed = existingUser.getListCompanyFollow();

        int start = (int)PageRequest.of(page -1,5).getOffset();
        int end = Math.min(start+5,listCompanyFollowed.size());
        List<Company> subList = listCompanyFollowed.subList(start,end);
        Page<Company> postPage = new PageImpl<>(subList,PageRequest.of(page-1,5),listCompanyFollowed.size());
        List<Company> companyList = postPage.getContent();

        model.addAttribute("companyList",companyList);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",postPage.getTotalPages());
        return "company_list_followed";
    }
}
