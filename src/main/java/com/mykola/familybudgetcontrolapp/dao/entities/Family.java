package com.mykola.familybudgetcontrolapp.dao.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "family")
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "restrictions_maximum_amount")
    private Long restrictionsMaximumAmount;

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<User> users;

}
