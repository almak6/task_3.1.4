package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findByID(Long id);

    User saveUser(User user);

    void deleteByID(Long id);

    User findByName(String name);

    User findByEmail(String email);
}
