package com.algostyle.backend.utils.auth;

import com.algostyle.backend.model.dto.userprofile.UserProfileDTO;
import com.algostyle.backend.model.entity.User;

public class UserUtil {

    // Convertir une entité User en userProfileDTO
    public static UserProfileDTO convertToProfileDTO(User user){
        return new UserProfileDTO(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getBio(),
                user.getProfilePictureUrl(),
                user.getProfilePictureFilename()
        );
    }

}
