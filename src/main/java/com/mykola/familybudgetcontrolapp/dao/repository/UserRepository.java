package com.mykola.familybudgetcontrolapp.dao.repository;

import com.mykola.familybudgetcontrolapp.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.restrictionsMaximumAmount = ?1 WHERE u.id = ?2")
    void addLimit(Long limit, Long id);
}
