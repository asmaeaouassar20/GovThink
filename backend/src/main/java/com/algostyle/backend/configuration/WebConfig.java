package com.algostyle.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// indique que cette classe est une configuration Spring
// Elle est utilisée pour personnaliser la configuration MVC de Spring Boot
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     *  Cette méthode configure les gestionnaires de ressources statiques
     *  Elle permet à Spring Boot de savoir où chercher les fichiers statiques
     *  ( HTML, CSS, JS, images ... )
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // *** Configuration pour servir les fichiers uploadés par l'utilisateur ***

        // Dans notre cas, tous les fichiers du dossier "uploads" sur le disque seront accessibles via l'URL http://localhost:8080/uploads/nom_fichier
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/") // "file:" = chemin vers un dossier sur le disque
                .setCachePeriod(3600); // Cache pendant 1 heure : ça veut dire lorsque quelqu'un télécharge un fichier depuis ton serveur (ex: une image dans /uploads/), le navigateur web va garder ce fichier en mémoire pendnat 3600s (1heure) avant de demander à nouveau au serveur
    }



}