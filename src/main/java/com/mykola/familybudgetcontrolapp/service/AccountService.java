package com.mykola.familybudgetcontrolapp.service;

import com.mykola.familybudgetcontrolapp.entities.Account;
import com.mykola.familybudgetcontrolapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public void update(Long balance, Long id) {
        accountRepository.update(balance, id);
    }
}
