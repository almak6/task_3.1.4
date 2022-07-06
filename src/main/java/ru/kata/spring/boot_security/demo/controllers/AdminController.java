package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping()
    public String index(Model model, Principal principal, User user) {
        User user2 = userServiceImpl.findByName(principal.getName());
        model.addAttribute("user2", user2);
        model.addAttribute("user", user);
        model.addAttribute("users", userServiceImpl.findAll());
        model.addAttribute("roleAdmin", roleServiceImpl.getAdminRole());
        model.addAttribute("roleUser", roleServiceImpl.getUserRole());
        return "/admin/user-list";
    }
    @PostMapping()
    public String createUser(User user) {
        userServiceImpl.saveUser(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userServiceImpl.deleteByID(user.getId());
        return "redirect:/admin";
    }
    @PatchMapping("/edit")
    public String update(@ModelAttribute("user") User user) {
        userServiceImpl.saveUser(user);
        return "redirect:/admin";
    }
}
