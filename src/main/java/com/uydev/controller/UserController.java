package com.uydev.controller;

import com.uydev.dto.UserDTO;
import com.uydev.services.CompanyService;
import com.uydev.services.RoleService;
import com.uydev.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("userRoles", roleService.getAllRolesForCreateUser());
        model.addAttribute("companies",companyService.getAllCompany());

        return "/user/user-create";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("newUser") UserDTO newUser){
        userService.createNewUser(newUser);
        return "redirect:/users/list";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long userId, Model model){
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("userRoles", roleService.getAllRolesForCreateUser());
        model.addAttribute("companies", companyService.getAllCompany());
        return "/user/user-update";
    }

    @PostMapping("/update/{id}")
    public String saveUpdatedUser(@ModelAttribute("user") UserDTO updatedUser){
        userService.updateUser(updatedUser);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long userId){
        userService.delete(userId);
        return "redirect:/users/list";
    }

}
