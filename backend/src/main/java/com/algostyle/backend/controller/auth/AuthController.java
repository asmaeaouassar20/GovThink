package com.algostyle.backend.controller.auth;


import com.algostyle.backend.model.dto.JwtResponse;
import com.algostyle.backend.model.dto.LoginRequest;
import com.algostyle.backend.model.dto.MyMessageResponse;
import com.algostyle.backend.model.dto.SignupRequest;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.service.UserService;
import com.algostyle.backend.utils.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="http://localhost:4200")
public class AuthController {



    @Autowired
    private UserService userService;


    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request){

        // Vérifier si l'email existe déjà
        if(userService.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        User user=userService.createUser(request);

        return ResponseEntity.ok(new MyMessageResponse("Utilisateur créé avec succès"));
    }





    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){

        // Authentification de l'utilisateur avec son email et son mot de passe
        User user=userService.authenticate(request.getEmail(),request.getPassword());
        if(user!=null){

            // Générer un JWT si l'authentification réussit
            String token = jwtUtil.generateToken(user.getEmail());

            // retourner le token dans la réponse
            return ResponseEntity.ok(new JwtResponse(token));

        }

        // Retourne une erreur 400 si les identifiants sont invalides
        return ResponseEntity.badRequest().body("Identifiants invalides");
    }










    @GetMapping("/profile")
    public ResponseEntity<?> currentUsername(Authentication authentication){
        System.out.println(">> "+authentication);
        if(authentication==null){
            return ResponseEntity.badRequest().body(new MyMessageResponse("Utilisateur non authentifié"));
        }

        // On récupère l'objet User
        User user = (User) authentication.getPrincipal();

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("nom", user.getNom());
        userInfo.put("prenom", user.getPrenom());
        userInfo.put("emai", user.getEmail());

        return ResponseEntity.ok(userInfo);
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        // RQ : La déconnexion réelle nécessiterait une gestion côté serveur du token7
        // ici, on se contente d'une réponse positive, le client doit supprimer le token
        return ResponseEntity.ok(new MyMessageResponse("Déconnecté avec succès"));
    }


}
