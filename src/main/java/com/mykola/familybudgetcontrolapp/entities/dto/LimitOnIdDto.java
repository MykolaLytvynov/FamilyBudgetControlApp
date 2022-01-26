package com.mykola.familybudgetcontrolapp.entities.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class LimitOnIdDto {
    private Long id;
    @Min(value = 1, message = "Limit should be greater than one")
    private Long limit;
}
