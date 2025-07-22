package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam int id) {
        userService.deleteUser( id);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam int id, @RequestParam String username,
                           @RequestParam String password, @RequestParam String email) {
        userService.editUser(id, username, password, email);
        return "redirect:/admin";
    }
}