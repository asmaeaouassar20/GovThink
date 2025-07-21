package com.algostyle.backend.utils.jwt;


import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Cette classe joue le rôle d'un filtre Spring Security qui intercepte chaque requête HTTP pour :
 * --> Vérifier la présence d'un JWT dans l'en-tête Authorization
 * --> Valider le token(signature, expiration)
 * --> Authentifier automatiquement l'utilisateur si le token est valide.
 */


@Component  // Déclare ce composant comme un filtre Spring qui ne s'exécute qu'une fois par requête
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Garantir que ce filtre s'exécute une seule fois par requête HTTP

    @Autowired
    private JwtUtil jwtUtil;    // Pour la manipulation des JWT


    @Autowired
    private UserService userService;


    // La méthode principale qui intercepte chaque requête HTTP
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1 - Extraction du token depuis l'en-tête Authorization
        String header = request.getHeader("Authorization");


        // 2 - Vérification de la présence et du format du token
        if(header !=null  && header.startsWith("Bearer ")){
            String token = header.substring(7);  // Supprimer "Bearer " pour obtenir le token pur

            // 3 - Validation du token
            if(jwtUtil.isTokenValid(token)){

                // 4 - Extraction de l'email depuis le token
                String email = jwtUtil.extractEmail(token);

                // Récupérer l'utilisateur complet (nom, prenom,..)
                User user= userService.findByEmail(email);

                // 5 - Création de l'objet d'authentification Spring Security
                UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(
                        user,   // on peut passer soit email soit user comme principal selon le besoin
                        null,
                        new ArrayList<>()
                );

                // 6 - Stockage de l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 7 - Poursuite de la chaîne de filtres
        filterChain.doFilter(request,response);
    }
}
