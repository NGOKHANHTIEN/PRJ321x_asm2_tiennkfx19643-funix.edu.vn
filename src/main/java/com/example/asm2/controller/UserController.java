package com.example.asm2.controller;

import com.example.asm2.entity.CV;
import com.example.asm2.entity.Company;
import com.example.asm2.entity.User;
import com.example.asm2.service.CompanyService;
import com.example.asm2.service.PostService;
import com.example.asm2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private CompanyService companyService;
    private PostService postService;


    @Autowired
    public UserController(UserService userService1, CompanyService companyService1, PostService postService1) {
        userService = userService1;
        companyService = companyService1;
        postService = postService1;

    }

    @GetMapping("/form-register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";

    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user,
                           @RequestParam("rePassword") String rePass) {
        if (!userService.isUserExist(user.getEmail())) {
            if (rePass.equals(user.getPassword())) {
                user.setPassword("{noop}" + user.getPassword());
                userService.addUser(user);
            }
        }

        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/getListApplicant")
    public String listApplicant(Model model) {
        List<User> list = userService.findAllApplicant();
        model.addAttribute("listApplicant", list);
        return "applicant_list";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userCurrent = null;
        if (auth.getPrincipal() instanceof UserDetails user) {
            String email = user.getUsername();
            System.out.println(email);
            userCurrent = userService.findByEmail(email);
        }
        model.addAttribute("user", userCurrent);
        if (userCurrent.getCompany() != null) {

            model.addAttribute("company", userCurrent.getCompany());
        } else {
            model.addAttribute("company", new Company());
        }
        return "profile";
    }

    @PostMapping("/uploadImage")
    public String upload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String fileName = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/image/" + fileName);
        Files.write(path, file.getBytes());
        model.addAttribute("avatar", fileName);
        return "profile";

    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute ("user")User user,
                                @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,
                                @RequestParam(value = "fileCv", required = false) MultipartFile fileCv,
                                Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }

         //Cập nhật thông tin cá nhân
        existingUser.setName(user.getName());
        existingUser.setPhoneNum(user.getPhoneNum());
        existingUser.setAddress(user.getAddress());
        existingUser.setDescription(user.getDescription());


        System.out.println(user.getId());

        // Xử lý ảnh đại diện nếu có tải lên
        if (fileImage != null && !fileImage.isEmpty()) {
            String fileNameImage = fileImage.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/image/" + fileNameImage);
            Files.write(imagePath, fileImage.getBytes());
            existingUser.setImage(fileNameImage);
        }

        // Xử lý file CV nếu có tải lên
        if (fileCv != null && !fileCv.isEmpty()) {
            String fileNameCv = fileCv.getOriginalFilename();
            Path cvPath = Paths.get("src/main/resources/static/uploadFile/" + fileNameCv);
            Files.write(cvPath, fileCv.getBytes());

            // Kiểm tra nếu user đã có CV thì cập nhật, nếu chưa có thì tạo mới
            CV cv = existingUser.getCv() != null ? existingUser.getCv() : new CV();
            cv.setFileName(fileNameCv);
            cv.setUser(existingUser);
            userService.save(cv);
        }

        // Lưu lại user
        userService.addUser(existingUser);

        model.addAttribute("user", user);
        return "profile";

    }

    @PostMapping("/update-profile-hr")
    public String updateProfile(@ModelAttribute User user,
                                @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,

                                Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }

        // Cập nhật thông tin cá nhân
        existingUser.setName(user.getName());
        existingUser.setPhoneNum(user.getPhoneNum());
        existingUser.setAddress(user.getAddress());
        existingUser.setDescription(user.getDescription());

        // Xử lý ảnh đại diện nếu có tải lên
        if (fileImage != null && !fileImage.isEmpty()) {
            String fileNameImage = fileImage.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/image/" + fileNameImage);
            Files.write(imagePath, fileImage.getBytes());
            existingUser.setImage(fileNameImage);
        }


        // Lưu lại user
        userService.addUser(existingUser);
        model.addAttribute("user", existingUser);
        if (existingUser.getCompany() != null) {

            model.addAttribute("company", existingUser.getCompany());
        } else {
            model.addAttribute("company", new Company());
        }
        return "profile";
    }

    @PostMapping("/update-company")
    public String updateCompany(@ModelAttribute Company company,
                                @RequestParam(value = "fileLogo", required = false) MultipartFile fileLogo,
                                Model model) throws Exception {
        if (fileLogo != null && !fileLogo.isEmpty()) {
            String fileNameImage = fileLogo.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/image/" + fileNameImage);
            Files.write(imagePath, fileLogo.getBytes());
            company.setLogo(fileNameImage);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = null;
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            existingUser = userService.findByEmail(email);
        }
        company.setUser(existingUser);
        companyService.add(company);
        return "profile";
    }

}
