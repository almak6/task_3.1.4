package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }



    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByID(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser(User user) {
//        User userFromDB = userRepository.findByName(user.getName());

        userRepository.save(user);
    }

    public void deleteByID(Long id) {
        userRepository.deleteById(id);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }
}
