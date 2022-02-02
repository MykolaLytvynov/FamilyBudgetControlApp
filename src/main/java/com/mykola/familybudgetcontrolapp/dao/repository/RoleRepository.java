package com.mykola.familybudgetcontrolapp.dao.repository;


import com.mykola.familybudgetcontrolapp.dao.entities.ERole;
import com.mykola.familybudgetcontrolapp.dao.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
