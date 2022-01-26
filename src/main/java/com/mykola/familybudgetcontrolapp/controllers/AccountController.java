package com.mykola.familybudgetcontrolapp.controllers;

import com.mykola.familybudgetcontrolapp.configuration.jwt.CurrentUser;
import com.mykola.familybudgetcontrolapp.entities.Account;
import com.mykola.familybudgetcontrolapp.entities.User;
import com.mykola.familybudgetcontrolapp.service.AccountService;
import com.mykola.familybudgetcontrolapp.service.UserService;
import com.mykola.familybudgetcontrolapp.service.jwt.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @PatchMapping("/cash-withdrawal")
    public synchronized ResponseEntity<String> cashWithdrawal(@CurrentUser UserDetailsImpl userDetails, @RequestBody Long count) {
        User user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        Account account = user.getFamily().getAccount();
        if (count < 0) {
            return ResponseEntity.ok("Error: Count should be greater than one");
        }

        if (count > account.getAmount()) {
            return ResponseEntity.ok("Error: Amount is bigger than Balance");
        }

        if (user.getFamily().getRestrictionsMaximumAmount() < (Long) count &&
                user.getFamily().getRestrictionsMaximumAmount() != 0) {
            return ResponseEntity.ok("Error: You've limit");
        }

        if (user.getRestrictionsMaximumAmount() < (Long) count &&
                user.getRestrictionsMaximumAmount() != 0) {
            return ResponseEntity.ok("Error: You've limit");
        }
        accountService.update(account.getAmount() - count, user.getFamily().getAccount().getId());
        return ResponseEntity.ok("Operation successful");
    }

    @PatchMapping("/refill")
    @PreAuthorize("hasRole('USER') or hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public synchronized ResponseEntity<String> refill(@CurrentUser UserDetailsImpl userDetails, @RequestBody Long count) {
        if (count < 0) {
            return ResponseEntity.ok("Error: Count should be greater than one");
        }
        Account result = userService.findByUsername(userDetails.getUsername()).get().getFamily().getAccount();
        accountService.update(result.getAmount() + count, result.getId());
        return ResponseEntity.ok("Operation successful");
    }
}
