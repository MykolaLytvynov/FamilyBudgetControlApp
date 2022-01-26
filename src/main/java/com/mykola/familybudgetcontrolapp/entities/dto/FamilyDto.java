package com.mykola.familybudgetcontrolapp.entities.dto;

import com.mykola.familybudgetcontrolapp.entities.Account;
import com.mykola.familybudgetcontrolapp.entities.Family;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class FamilyDto {
    private Long id;
    private String name;
    private Account account;
    private List<UserDto> users;

    public static FamilyDto fromUser(Family family) {
        FamilyDto familyDto = new FamilyDto(family.getId(),
                family.getName(),
                family.getAccount(),
                null
        );
        familyDto.setUsers(family.getUsers().stream().map(f -> UserDto.fromUser(f)).collect(Collectors.toList()));
        return familyDto;
    }
}
