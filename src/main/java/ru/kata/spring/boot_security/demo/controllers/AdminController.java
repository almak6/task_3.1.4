package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/admin/user-list";
    }
    @GetMapping("/create")
    public String createUserForm(User user, Model model) {
        model.addAttribute("roleAdmin", roleService.getAdminRole());
        model.addAttribute("roleUser", roleService.getUserRole());
        return "/admin/create";
    }
    @PostMapping()
    public String createUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.findByID(id).getRoles().clear();
        userService.deleteByID(id);
        return "redirect:/admin";
    }
    @GetMapping("/{id}/edit")
    public String editUserForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.findByID(id));
        model.addAttribute("roleAdmin", roleService.getAdminRole());
        model.addAttribute("roleUser", roleService.getUserRole());
        return "admin/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
        userService.saveUser(user);

        return "redirect:/admin";
    }
}
