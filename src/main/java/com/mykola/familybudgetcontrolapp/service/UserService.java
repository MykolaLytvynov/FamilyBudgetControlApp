package com.mykola.familybudgetcontrolapp.service;

import com.mykola.familybudgetcontrolapp.entities.User;
import com.mykola.familybudgetcontrolapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public void addLimit (Long limit, Long idUser) {
        userRepository.addLimit(limit, idUser);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }


}
