package com.algostyle.backend.service;

import com.algostyle.backend.model.dto.userprofile.UserProfileDTO;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ProfileService {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;


    public UserProfileDTO getCurrentUserProfile(){
        User user = getCurrentUser();
        return convertToProfileDTO(user);
    }


    /**
     * Met à jour l'utilisateur connecté
     */
    public UserProfileDTO updateCurrentUserProfile(UserProfileDTO userProfileDTO){
        User currentUser = getCurrentUser();

        // Vérifier si l'email n'est pas déjà utilisé par un autre utilisateur
        if(!currentUser.getEmail().equals(userProfileDTO.getEmail()) && userRepository.existsByEmailAndIdNot(userProfileDTO.getEmail(),currentUser.getId())){
            throw new RuntimeException("Cet email est déjà utilisé par un autre utilisateur");
        }

        // Mettre à jour les informations
        currentUser.setId(userProfileDTO.getId());
        currentUser.setPrenom(userProfileDTO.getPrenom());
        currentUser.setNom(userProfileDTO.getNom());
        currentUser.setEmail(userProfileDTO.getEmail());

        User updatedUser = this.userRepository.save(currentUser);
        return convertToProfileDTO(updatedUser);
    }






    // Changer le mot de passe de l'utilisateur connecté
    public void changePassword(String currentPassword, String newPassword){

            User currentUser = getCurrentUser();

            // Vérifier l'ancien mot de passe
        if(!passwordEncoder.matches(currentPassword,currentUser.getPassword())){
            throw new RuntimeException("Le mot de passe actuel est incorrect");
        }

        // Vérifier que le nouveau mot de passe est différent
        if(passwordEncoder.matches(newPassword,currentUser.getPassword())){
            throw new RuntimeException("le nouveau mot de passe doi être différent de l'ancien");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(currentUser);
    }





    // récupérer l'utilisateur actuellement connecté
    private User getCurrentUser(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println("email of current user : "+email);
        return userRepository.findByEmail(email);
    }



    // Convertir une entité User en userProfileDTO
    private UserProfileDTO convertToProfileDTO(User user){
        return new UserProfileDTO(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail()
        );
    }

}


