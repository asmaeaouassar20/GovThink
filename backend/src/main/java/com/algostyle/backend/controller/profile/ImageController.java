package com.algostyle.backend.controller.profile;



import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {


    @GetMapping("/profile-pictures/{filename}")
    public ResponseEntity<Resource> showImage(@PathVariable String filename){
        try{
            // Construire le chemin vers l'image
            String uploadDir = "uploads";
            Path imagePath = Paths.get(uploadDir).resolve("profile-pictures").resolve(filename).normalize();

            // Lire le fichier
            Resource resource = new UrlResource(imagePath.toUri());

            // Vérifier que le fichier existe et est lisible
            if(resource.exists() && resource.isReadable()){
                // Déterminer le type d'image
                String contentType = getImageType(filename);

                // retourner l'image
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+filename+"\"")
                        .body(resource);
            }else{
                // Fichier non trouvé
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){
            System.err.println("Erreur lors de la lecture de l'image: "+e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }




    private String getImageType(String filename){
        String extension=filename.substring(filename.lastIndexOf('.')+1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> "image/jpeg";
        };
    }
}
