package com.mykola.familybudgetcontrolapp.service;

import com.mykola.familybudgetcontrolapp.entities.Family;
import com.mykola.familybudgetcontrolapp.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;

    public Family getFamilyById(Long id) {
        return familyRepository.findById(id).orElse(null);
    }

    public void addLimit(Long limit, Long idFamily) {
        familyRepository.addLimit(limit, idFamily);
    }

    public List<Family> getAll() {
        return familyRepository.findAll();
    }
}
