package com.mykola.familybudgetcontrolapp.api.controllers;

import com.mykola.familybudgetcontrolapp.configuration.jwt.CurrentUser;
import com.mykola.familybudgetcontrolapp.dao.entities.Account;
import com.mykola.familybudgetcontrolapp.dao.entities.User;
import com.mykola.familybudgetcontrolapp.bl.service.AccountService;
import com.mykola.familybudgetcontrolapp.bl.service.UserService;
import com.mykola.familybudgetcontrolapp.api.dto.jwt.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if (count < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Count should be greater than one");
        }
        User user = userService.findByUsername(userDetails.getUsername()).get();
        accountService.cashWithdrawal(user, count);
        return ResponseEntity.ok("Operation successful");
    }

    @PatchMapping("/refill")
    @PreAuthorize("hasRole('USER') or hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public synchronized ResponseEntity<String> refill(@CurrentUser UserDetailsImpl userDetails, @RequestBody Long count) {
        if (count < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Count should be greater than one");
        }
        Account result = userService.findByUsername(userDetails.getUsername()).get().getFamily().getAccount();
        accountService.update(result.getAmount() + count, result.getId());
        return ResponseEntity.ok("Operation successful");
    }
}
