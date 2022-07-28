package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Collection;

public interface RoleService {

    Collection<Role> findAllUserRoles(User user);

    Role findRoleByName(String name);

    Role getAdminRole();

    Role getUserRole();
}
