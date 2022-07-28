package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.Collection;
import java.util.HashSet;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Collection<Role> findAllUserRoles(User user) {
        Collection<Role> roles = new HashSet<>();
        user.getRoles().stream().forEach(r -> roles.add(findRoleByName(r.getName())));
        return roles;
    }

    public Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    public Role getAdminRole() {
        return roleRepository.findByNameIgnoreCase("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
    }

    public Role getUserRole() {
        return roleRepository.findByNameIgnoreCase("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
    }
}
