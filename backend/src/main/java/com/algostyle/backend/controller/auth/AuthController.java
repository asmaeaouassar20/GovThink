package com.algostyle.backend.controller.auth;


import com.algostyle.backend.model.dto.auth.JwtResponse;
import com.algostyle.backend.model.dto.auth.LoginRequest;
import com.algostyle.backend.model.dto.auth.MyMessageResponse;
import com.algostyle.backend.model.dto.auth.SignupRequest;
import com.algostyle.backend.model.dto.userprofile.ApiResponse;
import com.algostyle.backend.model.dto.userprofile.UserProfileDTO;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.service.UserService;
import com.algostyle.backend.utils.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> signup(
            @RequestParam("userData") String userDataJson,
            @RequestParam(value = "profilePicture", required = false)MultipartFile profilePicture
            ){

       try{
           // convertir le JSON en objet
           ObjectMapper mapper=new ObjectMapper();

           SignupRequest signupRequest=mapper.readValue(userDataJson, SignupRequest.class);

           UserProfileDTO newUser = userService.createUser(signupRequest, profilePicture);
           return ResponseEntity.ok(ApiResponse.success("Inscripiton réussie",newUser));
       }catch (RuntimeException e){
           return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Erreur lors de linscription"));
       }
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
