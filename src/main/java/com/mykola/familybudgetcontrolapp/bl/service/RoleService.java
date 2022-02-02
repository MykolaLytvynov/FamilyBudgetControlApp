package com.mykola.familybudgetcontrolapp.bl.service;

import com.mykola.familybudgetcontrolapp.dao.entities.ERole;
import com.mykola.familybudgetcontrolapp.dao.entities.Role;
import com.mykola.familybudgetcontrolapp.dao.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    Optional<Role> findByName(ERole name){
        return roleRepository.findByName(name);
    }
}
