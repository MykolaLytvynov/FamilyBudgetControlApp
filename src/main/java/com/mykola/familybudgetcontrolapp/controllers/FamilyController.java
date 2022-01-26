package com.mykola.familybudgetcontrolapp.controllers;

import com.mykola.familybudgetcontrolapp.configuration.jwt.CurrentUser;
import com.mykola.familybudgetcontrolapp.entities.User;
import com.mykola.familybudgetcontrolapp.entities.dto.LimitOnIdDto;
import com.mykola.familybudgetcontrolapp.service.FamilyService;
import com.mykola.familybudgetcontrolapp.service.UserService;
import com.mykola.familybudgetcontrolapp.service.jwt.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addLimitOnFamily(@CurrentUser UserDetailsImpl userDetails, @RequestBody @Valid LimitOnIdDto limitOnIdDto) {
        User user = userService.findByUsername(userDetails.getUsername()).get();
        if (limitOnIdDto.getId() == user.getFamily().getId()) {
            familyService.addLimit(limitOnIdDto.getLimit(), limitOnIdDto.getId());
        }
    }

    @PatchMapping("/global/make-limits-on-family")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    public ResponseEntity<String> addLimitOnFamilyFromGlogalAdmin(@RequestBody @Valid LimitOnIdDto limitOnIdDto) {
        if (familyService.getAll().stream().anyMatch(family -> family.getId() == limitOnIdDto.getId())) {
            familyService.addLimit(limitOnIdDto.getLimit(), limitOnIdDto.getId());
            return ResponseEntity.ok("Limit added successfully");
        } else return ResponseEntity.ok("Family not found");
    }

    @PatchMapping("/global/make-limits-on-all-family")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    public ResponseEntity<String> addLimitsOnAllfamilyFromGlogalAdmin(@RequestBody Long limit) {
        if (limit < 0) {
            return ResponseEntity.ok("Error: limit should be greater than one");
        }
        familyService.getAll().forEach(family -> familyService.addLimit(limit, family.getId()));
        return ResponseEntity.ok("Limit added successfully");
    }


}
