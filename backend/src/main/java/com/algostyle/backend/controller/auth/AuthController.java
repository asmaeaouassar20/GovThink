package com.algostyle.backend.controller.auth;


import com.algostyle.backend.model.dto.MyMessageResponse;
import com.algostyle.backend.model.dto.SignupRequest;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="http://localhost:4200")
public class AuthController {



    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request){

        // Vérifier si l'email existe déjà
        if(userService.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        User user=userService.createUser(request);

        return ResponseEntity.ok(new MyMessageResponse("Utilisateur créé avec succès"));
    }
}
