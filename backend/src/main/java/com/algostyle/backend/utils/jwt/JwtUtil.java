package com.algostyle.backend.utils.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * Cette classe est un composant Spring qui gère ce qui concerne les tokens JWT
 *  (JSON Web Tokens) dans une application.
 */



@Component     // Déclarer cette classe comme un composant Spring (géré par le conteneur IoC)
public class JwtUtil {

    // clé secrète pour signer les tokens
    private String secret = "UneChaineSuperSecuriseeAvecAuMoins64CaracteresCar512Bits=64OctetsUneChaineSuperSecuriseeAvecAuMoins64CaracteresCar512Bits=64Octets";





    // génération du token en utilisant l'email de l'utilisateur
    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .signWith(SignatureAlgorithm.HS512,secret)  // C'est l'algorithme de signature
                .compact(); // Pour générer le token sous forme de chaîne
    }



    public boolean isTokenValid(String token){
        try{
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return  false;
        }
    }




    public String extractEmail(String token){
        return Jwts.parser()
                .setSigningKey(secret)  // Configurer la clé de vérification
                .parseClaimsJws(token)  // Parser et valider le token
                .getBody()      // Récupérer le contenu (claims)
                .getSubject();  // Extraire le sujet (email)
    }


}
