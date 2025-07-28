package com.algostyle.backend.service;


import com.algostyle.backend.model.dto.auth.SignupRequest;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; //Pour le hachage sécurisé des mots de passe


    public User createUser(SignupRequest request){
        User user=new User();
        user.setEmail(request.getEmail());
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
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
