package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @GetMapping("/")
    public String showHomePage() {
        return "index"; // вернёт шаблон index.html
    }

    @GetMapping("/index")
    public String indexRedirect() {
        return "index";
    }
}