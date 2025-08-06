package com.algostyle.backend.service;

import com.algostyle.backend.model.dto.userprofile.UpdateProfileDTO;
import com.algostyle.backend.model.dto.userprofile.UserProfileDTO;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.UserRepository;
import com.algostyle.backend.utils.auth.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileService {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;


    public UserProfileDTO getCurrentUserProfile(){
        User user = getCurrentUser();
        System.out.println("** current user **  :  "+user);
        return UserUtil.convertToProfileDTO(user);
    }


    /**
     * Met à jour l'utilisateur connecté
     */
    public UserProfileDTO updateCurrentUserProfile(UpdateProfileDTO updateProfileDTO){

        User currentUser = getCurrentUser();

        // Vérifier si l'email n'est pas déjà utilisé par un autre utilisateur
        if(!currentUser.getEmail().equals(updateProfileDTO.getEmail()) && userRepository.existsByEmailAndIdNot(updateProfileDTO.getEmail(),currentUser.getId())){
            throw new RuntimeException("Cet email est déjà utilisé par un autre utilisateur");
        }

        System.out.println("currentUser : "+currentUser);
        System.out.println("userProfileDTO : "+updateProfileDTO);

        // Mettre à jour les informations
        currentUser.setPrenom(updateProfileDTO.getPrenom());
        currentUser.setNom(updateProfileDTO.getNom());
        currentUser.setEmail(updateProfileDTO.getEmail());
        currentUser.setBio(updateProfileDTO.getBio());

        User updatedUser = this.userRepository.save(currentUser);
        return UserUtil.convertToProfileDTO(updatedUser);
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
        return (User) authentication.getPrincipal();
    }






    // Chang la photo de profile
    public UserProfileDTO updateProfilePicture(MultipartFile file){
        User currentUser = getCurrentUser();

        try{
            // Supprimer l'ancienne photo si elle existe
            if(currentUser.getProfilePictureFilename()!=null){
                fileStorageService.deleteProfilePicture(currentUser.getProfilePictureFilename());
            }

            // Sauvegarder la nouvelle photo
            String filePath = fileStorageService.saveProfilePicture(file);
            String imageUrl = fileStorageService.createImageUrl(filePath);

            // Mettre à jour l'utilisateur
            currentUser.setProfilePictureFilename(filePath);
            currentUser.setProfilePictureUrl(imageUrl);

            User updatedUser = userRepository.save(currentUser);
            return UserUtil.convertToProfileDTO(updatedUser);
        }
        catch (Exception e){
            throw new RuntimeException("Erreur lors de l'upload : "+e.getMessage());
        }
    }



    // Supprimer le photo de profil
    public UserProfileDTO deleteProfilePicture() throws Exception{
        User currentUser = getCurrentUser();

        // Supprimer le fichier
        if(currentUser.getProfilePictureFilename()!=null){
            fileStorageService.deleteProfilePicture(currentUser.getProfilePictureFilename());
        }

        // Mettre à jour l'utilisateur
        currentUser.setProfilePictureFilename(null);
        currentUser.setProfilePictureUrl(null);
        User updatedUser = this.userRepository.save(currentUser);
        return UserUtil.convertToProfileDTO(updatedUser);
    }



}


