package com.mykola.familybudgetcontrolapp.bl.service;

import com.mykola.familybudgetcontrolapp.api.dto.UserDto;
import com.mykola.familybudgetcontrolapp.dao.entities.User;
import com.mykola.familybudgetcontrolapp.dao.repository.UserRepository;
import com.mykola.familybudgetcontrolapp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FamilyService familyService;

    public UserDto getUserById(Long id) {
        NotFoundException notFoundException = new NotFoundException("User not found by id = " + id);
        if (!userRepository.existsById(id)) throw notFoundException;
        User result = userRepository.findById(id).get();
        return UserDto.fromUser(result);
    }

    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public void addLimit(Long limit, Long idUser) {
        userRepository.addLimit(limit, idUser);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.fromUser(user))
                .collect(Collectors.toList());
    }

    public Long getBalance(Long idUser) {
        NotFoundException notFoundException = new NotFoundException("User not found by id = " + idUser);
        if (!userRepository.existsById(idUser)) throw notFoundException;
        User user = userRepository.findById(idUser).get();
        user.getFamily().getAccount().getAmount();
        return user.getFamily().getAccount().getAmount();
    }

    public boolean cheakUserByFamily(Long idUser, Long idFamily) {
        List<UserDto> users = familyService.getUsersFamily(idFamily);
        for (UserDto u : users) {
            if (u.getId() == idUser) {
                return true;
            }
        }
        return false;
    }

    public void addLimitOnAnyPerson(Long idUser, Long limit) {
        NotFoundException notFoundException = new NotFoundException("User not found by id = " + idUser);
        if(userRepository.existsById(idUser)) {
            userRepository.addLimit(limit, idUser);
        } else throw notFoundException;
    }


}
