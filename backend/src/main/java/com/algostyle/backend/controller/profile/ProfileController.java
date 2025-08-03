package com.algostyle.backend.controller.profile;

import com.algostyle.backend.model.dto.userprofile.ApiResponse;
import com.algostyle.backend.model.dto.userprofile.ChangePasswordDTO;
import com.algostyle.backend.model.dto.userprofile.UserProfileDTO;
import com.algostyle.backend.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {
    @Autowired
    private ProfileService profileService;


    // Récupérer e profile de l'utilisateur connecté
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserProfileDTO>>  getCurrentProfile(){
        try{
            UserProfileDTO profile=profileService.getCurrentUserProfile();
            return ResponseEntity.ok(ApiResponse.success("profil récupéré avec succès",profile));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Erreur lors de la récupération du profil: "+e.getMessage()));
        }
    }



    // Met à jour le profil de l'utilisateur connecté
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateProfile(
            @Valid @RequestBody UserProfileDTO updateDTO
    ){
        try{
            UserProfileDTO updatedProfile = this.profileService.updateCurrentUserProfile(updateDTO);
            return ResponseEntity.ok(ApiResponse.success("Utilisateur mis à jour avec succès",updatedProfile));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Erreur lors de la mise à jour de l'utilisateur "+e.getMessage()));
        }
    }


    // Change le mot de passe de l'utilisateur connecté
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO
            ){

        // Vérifie que les mots de passe correspondent
        if(!changePasswordDTO.isPasswordConfirmed()){
            return ResponseEntity.badRequest().body(ApiResponse.error("Le nouveau mot de passe et sa confirmation ne correspondent pas"));
        }

        try{
            profileService.changePassword(changePasswordDTO.getCurrentPassword(),changePasswordDTO.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success("Mot de passe changé avec succès",null));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }

    }
}
