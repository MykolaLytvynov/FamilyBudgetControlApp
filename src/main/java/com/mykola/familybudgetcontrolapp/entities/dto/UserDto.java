package com.mykola.familybudgetcontrolapp.entities.dto;


import com.mykola.familybudgetcontrolapp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;


    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto(user.getId(),
                user.getUsername(),
        user.getEmail());
        return userDto;
    }
}
