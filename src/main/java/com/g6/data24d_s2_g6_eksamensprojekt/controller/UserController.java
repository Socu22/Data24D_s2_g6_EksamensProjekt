package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/Login")
    public String loginPage() {
        return "login";
    }
}
