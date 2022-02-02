package com.mykola.familybudgetcontrolapp.bl.service;

import com.mykola.familybudgetcontrolapp.api.dto.FamilyDto;
import com.mykola.familybudgetcontrolapp.api.dto.UserDto;
import com.mykola.familybudgetcontrolapp.dao.entities.Family;
import com.mykola.familybudgetcontrolapp.dao.repository.FamilyRepository;
import com.mykola.familybudgetcontrolapp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;

    public FamilyDto getFamilyById(Long id) {
        NotFoundException notFoundException = new NotFoundException("Family not found by id = " + id);
        if(!familyRepository.existsById(id)) throw notFoundException;
        Family result = familyRepository.findById(id).get();
        return FamilyDto.fromFamily(result);
    }

    public List<FamilyDto> getAll() {
        return familyRepository.findAll().stream()
                .map(family -> FamilyDto.fromFamily(family))
                .collect(Collectors.toList());
    }

    public List<UserDto> getUsersFamily(Long idFamily) {
        NotFoundException notFoundException = new NotFoundException("Family not found by id = " + idFamily);
        if(!familyRepository.existsById(idFamily)) throw notFoundException;
        Family family = familyRepository.findById(idFamily).get();
        List<UserDto> result = family.getUsers().stream().map(user -> UserDto.fromUser(user)).collect(Collectors.toList());
        return result;
    }

    public void addLimitForYourFamily(Long familyLimit, Long idFamily) {
        familyRepository.addLimit(familyLimit, idFamily);
    }

    public void addLimitToAnyFamily(Long familyLimit, Long idFamily) {
        if (familyRepository.findAll().stream().anyMatch(family -> family.getId() == idFamily)) {
            familyRepository.addLimit(familyLimit, idFamily);
        } else throw new NotFoundException("Family not found");
    }

    public void addLimitsOnAllfamily(Long limit) {
        familyRepository.findAll().forEach(family -> familyRepository.addLimit(limit, family.getId()));
    }

}
