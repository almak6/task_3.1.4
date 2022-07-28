package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.security.Principal;

@Controller
public class LoginController {

    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    public LoginController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }
    @GetMapping("/")
    public String loginPage() {
        return "/login";
    }
    @GetMapping("/admin")
    public String index(Model model, Principal principal, User user) {
        User user2 = userServiceImpl.findByName(principal.getName());
        model.addAttribute("user2", user2);
        model.addAttribute("user", user);
        model.addAttribute("users", userServiceImpl.findAll());
        model.addAttribute("roleAdmin", roleServiceImpl.getAdminRole());
        model.addAttribute("roleUser", roleServiceImpl.getUserRole());
        return "/admin/user-list";
    }

}
