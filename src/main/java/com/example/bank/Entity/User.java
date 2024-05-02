package com.example.bank.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String accountNumber;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private Double cash;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private Boolean is_active;
    private LocalDate created_at;
    private LocalDate birthdate;
    @Transient
    private Integer age;

    public Integer getAge() {
        if (birthdate != null) {
            return Period.between(birthdate, LocalDate.now()).getYears();
        }
        return null;
    }
}
