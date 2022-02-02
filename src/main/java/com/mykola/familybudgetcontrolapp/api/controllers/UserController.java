package com.mykola.familybudgetcontrolapp.api.controllers;


import com.mykola.familybudgetcontrolapp.bl.service.FamilyService;
import com.mykola.familybudgetcontrolapp.configuration.jwt.JwtUtils;
import com.mykola.familybudgetcontrolapp.api.dto.jwt.LoginResponse;
import com.mykola.familybudgetcontrolapp.api.dto.jwt.LoginRequest;
import com.mykola.familybudgetcontrolapp.api.dto.LimitOnIdDto;
import com.mykola.familybudgetcontrolapp.api.dto.UserDto;
import com.mykola.familybudgetcontrolapp.configuration.jwt.CurrentUser;
import com.mykola.familybudgetcontrolapp.bl.service.UserService;
import com.mykola.familybudgetcontrolapp.api.dto.jwt.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private FamilyService familyService;
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

        return ResponseEntity.ok(new LoginResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUser(@CurrentUser UserDetailsImpl userDetails) {
        UserDto result = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/myfamily")
    @PreAuthorize("hasRole('USER') or hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUserFamily(@CurrentUser UserDetailsImpl userDetails) {
        List<UserDto> result = familyService.getUsersFamily(userDetails.getFamily().getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mybalance")
    @PreAuthorize("hasRole('USER') or hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Long> getUserBalance(@CurrentUser UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getBalance(userDetails.getId()));
    }

    @PatchMapping("/make-limits-on-person")
    @PreAuthorize("hasRole('GLOBAL_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<String> addLimitOnPerson(@CurrentUser UserDetailsImpl userDetails, @RequestBody @Valid LimitOnIdDto limitOnIdDto) {
        if(userService.cheakUserByFamily(limitOnIdDto.getId(), userDetails.getFamily().getId())) {
            userService.addLimit(limitOnIdDto.getLimit(), limitOnIdDto.getId());
            return ResponseEntity.ok("Limit added successfully");
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not belong to your family");

    }

    @PatchMapping("/global/make-limits-on-person")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    public ResponseEntity<String> addLimitOnAnyPerson(@RequestBody @Valid LimitOnIdDto limitOnIdDto) {
        userService.addLimitOnAnyPerson(limitOnIdDto.getId(), limitOnIdDto.getLimit());
        return ResponseEntity.ok("Limit added successfully");
    }


}
