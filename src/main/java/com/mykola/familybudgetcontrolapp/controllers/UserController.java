package com.mykola.familybudgetcontrolapp.controllers;


import com.mykola.familybudgetcontrolapp.configuration.jwt.JwtUtils;
import com.mykola.familybudgetcontrolapp.entities.POJOForLogin.JwtResponse;
import com.mykola.familybudgetcontrolapp.entities.POJOForLogin.LoginRequest;
import com.mykola.familybudgetcontrolapp.entities.User;
import com.mykola.familybudgetcontrolapp.entities.dto.FamilyDto;
import com.mykola.familybudgetcontrolapp.entities.dto.LimitOnIdDto;
import com.mykola.familybudgetcontrolapp.entities.dto.UserDto;
import com.mykola.familybudgetcontrolapp.configuration.jwt.CurrentUser;
import com.mykola.familybudgetcontrolapp.service.UserService;
import com.mykola.familybudgetcontrolapp.service.jwt.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUser(@CurrentUser UserDetailsImpl userDetails) {
        User result = userService.findByUsername(userDetails.getUsername()).get();
        return ResponseEntity.ok(UserDto.fromUser(result));
    }

    @GetMapping("/myfamily")
    @PreAuthorize("hasRole('USER') or hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUserFamily(@CurrentUser UserDetailsImpl userDetails) {
        User result = userService.findByUsername(userDetails.getUsername()).get();
        return ResponseEntity.ok(FamilyDto.fromUser(result.getFamily()).getUsers());
    }

    @GetMapping("/mybalance")
    @PreAuthorize("hasRole('USER') or hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Long> getUserBalance(@CurrentUser UserDetailsImpl userDetails) {
        User result = userService.findByUsername(userDetails.getUsername()).get();
        return ResponseEntity.ok(FamilyDto.fromUser(result.getFamily()).getAccount().getAmount());
    }

    @PatchMapping("/make-limits-on-person")
    @PreAuthorize("hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public void addLimitOnPerson(@CurrentUser UserDetailsImpl userDetails, @RequestBody @Valid LimitOnIdDto limitOnIdDto) {
        User user = userService.findByUsername(userDetails.getUsername()).get();
        for (User u : user.getFamily().getUsers()) {
            if (u.getId() == limitOnIdDto.getId()) {
                userService.addLimit(limitOnIdDto.getLimit(), limitOnIdDto.getId());
            }
        }
    }

    @PatchMapping("/global/make-limits-on-person")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    public ResponseEntity<String> addLimitOnAnyPerson(@RequestBody @Valid LimitOnIdDto limitOnIdDto) {
        if (userService.getAll().stream().anyMatch(user -> user.getId() == limitOnIdDto.getId())) {
            userService.addLimit(limitOnIdDto.getLimit(), limitOnIdDto.getId());
            return ResponseEntity.ok("Limit added successfully");
        } else return ResponseEntity.ok("User not found");
    }


}
