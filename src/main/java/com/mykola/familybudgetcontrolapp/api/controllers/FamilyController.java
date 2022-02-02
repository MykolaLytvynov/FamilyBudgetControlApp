package com.mykola.familybudgetcontrolapp.api.controllers;

import com.mykola.familybudgetcontrolapp.configuration.jwt.CurrentUser;
import com.mykola.familybudgetcontrolapp.dao.entities.User;
import com.mykola.familybudgetcontrolapp.api.dto.LimitOnIdDto;
import com.mykola.familybudgetcontrolapp.bl.service.FamilyService;
import com.mykola.familybudgetcontrolapp.bl.service.UserService;
import com.mykola.familybudgetcontrolapp.api.dto.jwt.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class FamilyController {
    @Autowired
    private UserService userService;
    @Autowired
    private FamilyService familyService;

    @PatchMapping("make-limits-on-family")
    @PreAuthorize("hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<String> addLimitOnFamily(@CurrentUser UserDetailsImpl userDetails, @RequestBody Long limit) {
        if (limit < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: limit should be greater than one");
        }
        User user = userService.findByUsername(userDetails.getUsername()).get();
        familyService.addLimitForYourFamily(limit, user.getFamily().getId());
        return ResponseEntity.ok("Limit added successfully");
    }

    @PatchMapping("/global/make-limits-on-family")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    public ResponseEntity<String> addLimitOnFamilyFromGlogalAdmin(@RequestBody @Valid LimitOnIdDto limitOnIdDto) {
        familyService.addLimitToAnyFamily(limitOnIdDto.getLimit(), limitOnIdDto.getId());
        return ResponseEntity.ok("Limit added successfully");
    }

    @PatchMapping("/global/make-limits-on-all-family")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    public ResponseEntity<String> addLimitsOnAllfamilyFromGlogalAdmin(@RequestBody Long limit) {
        if (limit < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: limit should be greater than one");
        }
        familyService.addLimitsOnAllfamily(limit);
        return ResponseEntity.ok("Limit added successfully");
    }


}
