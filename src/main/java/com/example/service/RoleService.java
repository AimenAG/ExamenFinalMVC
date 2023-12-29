package com.example.service;
import com.example.repositories.RoleRepository;
import com.example.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }


    @PostConstruct
    public void initRoles() {
        List<String> roleNames = Arrays.asList("Invite", "Regulier", "Premium", "Admin");

        for (String roleName : roleNames) {
            Role role = roleRepository.findByRoleName(roleName);
            if (role == null) {
                role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);
            }
        }
    }
}