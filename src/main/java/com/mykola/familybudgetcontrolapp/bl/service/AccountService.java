package com.mykola.familybudgetcontrolapp.bl.service;

import com.mykola.familybudgetcontrolapp.dao.entities.Account;
import com.mykola.familybudgetcontrolapp.dao.entities.User;
import com.mykola.familybudgetcontrolapp.dao.repository.AccountRepository;
import com.mykola.familybudgetcontrolapp.exception.LimitException;
import com.mykola.familybudgetcontrolapp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account getAccountById(Long id) {
        NotFoundException notFoundException = new NotFoundException("Account not found by id = " + id);
        Account result = accountRepository.findById(id).orElse(null);
        if (result == null) throw notFoundException;
        return result;
    }

    public void update(Long balance, Long id) {
        accountRepository.update(balance, id);
    }

    public void cashWithdrawal(User user, Long count) {
        Account account = user.getFamily().getAccount();
        Long userLimit = user.getRestrictionsMaximumAmount();
        Long familyLimit = user.getFamily().getRestrictionsMaximumAmount();

        if (count > account.getAmount()) {
            throw new LimitException("Amount is bigger than Balance");
        }

        if (familyLimit < (Long) count && familyLimit != 0) {
            throw new LimitException("You've family limit: " + familyLimit);
        }

        if (userLimit < (Long) count && userLimit != 0) {
            throw new LimitException("You've limit: " + userLimit);
        }
        accountRepository.update(account.getAmount() - count, user.getFamily().getAccount().getId());
    }
}
