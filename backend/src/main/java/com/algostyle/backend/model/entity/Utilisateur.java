package com.algostyle.backend.model.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="Utilisateur")
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prenom;
    private String nom;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true,nullable=false)
    private String email;
}
