package com.mykola.familybudgetcontrolapp.dao.repository;

import com.mykola.familybudgetcontrolapp.dao.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.amount = ?1 WHERE a.id = ?2")
    void update(Long balance, Long id);
}
