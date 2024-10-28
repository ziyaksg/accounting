package com.uydev.controller;

import com.uydev.dto.UserDTO;
import com.uydev.services.CompanyService;
import com.uydev.services.RoleService;
import com.uydev.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('Admin','Root User')")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;

    @GetMapping("/list")
    public String getUserList(Model model){
       model.addAttribute("users", userService.getAllUsers()) ;
       return "/user/user-list";
    }

    @GetMapping("/create")
    public String createUser(Model model){
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("userRoles", roleService.getAllRoleForRoot());
        model.addAttribute("companies",companyService.getAllCompany());

        return "/user/user-create";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("newUser") UserDTO newUser){
        userService.createNewUser(newUser);
        return "redirect:/users/list";
    }

}
