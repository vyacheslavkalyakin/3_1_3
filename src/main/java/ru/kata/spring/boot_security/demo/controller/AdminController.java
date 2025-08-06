package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                model.addAttribute("username", userDetails.getUsername());
                model.addAttribute("authorities", userDetails.getAuthorities());
            } else {
                model.addAttribute("username", principal.toString());
            }
        }
        return "admin";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                model.addAttribute("username", userDetails.getUsername());
                model.addAttribute("authorities", userDetails.getAuthorities());
            } else {
                model.addAttribute("username", principal.toString());
            }
        }
        return "new-user";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("roles") List<Integer> roleIds) {
        Set<Role> userRoles = roleIds.stream().map(roleService::getRoleById)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        user.setRoles(userRoles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam int id, @RequestParam String password,
                           @RequestParam String email, @RequestParam String firstName,
                           @RequestParam String lastName, @RequestParam int age) {
        userService.editUser(id, password, email, firstName, lastName, age);
        return "redirect:/admin";
    }
}