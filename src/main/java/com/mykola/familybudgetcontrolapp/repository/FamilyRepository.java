package com.mykola.familybudgetcontrolapp.repository;

import com.mykola.familybudgetcontrolapp.entities.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Family f SET f.restrictionsMaximumAmount = ?1 WHERE f.id = ?2")
    void addLimit(Long limit, Long id);
}
