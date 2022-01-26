package com.mykola.familybudgetcontrolapp.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long number;
    private Long amount;
}
