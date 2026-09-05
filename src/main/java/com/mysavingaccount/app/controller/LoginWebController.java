package com.mysavingaccount.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginWebController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // looks for src/main/resources/templates/login.html
    }
}