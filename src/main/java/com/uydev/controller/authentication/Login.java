package com.uydev.controller.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class Login {

    @PostMapping("/login")
    public String Login(){
        return "/company/list";
    }
}