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
}
