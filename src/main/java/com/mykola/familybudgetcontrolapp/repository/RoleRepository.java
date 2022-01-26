package com.mykola.familybudgetcontrolapp.repository;


import com.mykola.familybudgetcontrolapp.entities.ERole;
import com.mykola.familybudgetcontrolapp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
