package com.example.application.data.service;

import com.example.application.data.entity.Role;
import com.example.application.data.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
