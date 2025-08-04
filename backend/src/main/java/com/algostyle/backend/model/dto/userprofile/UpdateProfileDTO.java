package com.algostyle.backend.model.dto.userprofile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProfileDTO {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email est invalide")
    private String email;

    @Size(max = 100,message = "Le prénom ne peut pas dépasser 100 caractères")
    private String prenom;

    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String nom;

    private String bio;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString(){
        return "nom:"+nom+" - prenom:"+prenom+" - email:"+email+" .\n";
    }
}
