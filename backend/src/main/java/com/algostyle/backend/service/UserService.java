package com.algostyle.backend.service;


import com.algostyle.backend.model.dto.auth.SignupRequest;
import com.algostyle.backend.model.dto.userprofile.UserProfileDTO;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.UserRepository;
import com.algostyle.backend.utils.auth.UserUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserService
{


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder; //Pour le hachage sécurisé des mots de passe




    // Créer un nouveau utilisateur avec une photo optionnelle
    public UserProfileDTO createUser(SignupRequest request, MultipartFile profilePicture){

        // Vérifier si l'utilisateur existe déjà
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Cet email est déjà pris");
        }

        User user=new User();
        user.setEmail(request.getEmail());
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBio(request.getBio());

        if(profilePicture!=null && !profilePicture.isEmpty()){
            try{
                String filePath = fileStorageService.saveProfilePicture(profilePicture);
                String imageUrl = fileStorageService.createImageUrl(filePath);
                user.setProfilePictureFilename(filePath);
                user.setProfilePictureUrl(imageUrl);
            }catch (Exception e){
                // Log l'erreur mais ne pas faire échouer l'inscription
                System.err.println("Erreur upload photo : "+e.getMessage());
            }
        }

        User registeredUser = userRepository.save(user);
        return UserUtil.convertToProfileDTO(registeredUser);
    }


    // Créer un nouveau utilisateur sans photo de profil
    public UserProfileDTO createUser(SignupRequest signupRequest){
        return createUser(signupRequest, null);
    }


    public User authenticate(String email, String password){
        User user= userRepository.findByEmail(email);
        if(user!=null && passwordEncoder.matches(password,user.getPassword())){
            return user;
        }
        return null;
    }


    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }


    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }




}
