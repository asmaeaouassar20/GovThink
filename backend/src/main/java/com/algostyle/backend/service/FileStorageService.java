package com.algostyle.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    // Répertoire où on stocke les fichiers
    private final String uploadDir = "uploads";

    // Types d'images autorisés
    private final List<String> allowedTypes = Arrays.asList(
      "image/jpeg", "image/png" , "image/gif"
    );

    // Taille maximale : 5MB
    private final long maxSize = 5 * 1024 * 1024;


    /**
     * Sauvegarder une image sur le serveur
     * @param file le fichier envoyé par l'utilisateur
     * @return le chemin du fichier sauvegardé
     * @throws IOException
     */
    public String saveProfilePicture(MultipartFile file) throws IOException {
        // Vérifier que le fichier est valide
        checkFile(file);

        // Créer le dossier s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir, "profile-pictures");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Créer un nom unique pour éviter les conflits
        String originalName = file.getOriginalFilename();
        String extension = getFileExtension(originalName);
        String uniqueName = UUID.randomUUID().toString() + extension;

        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(uniqueName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Retourner le chemin relatif
        return "profile-pictures/" + uniqueName;
    }

    private void checkFile(MultipartFile file){
        if(file.isEmpty()){
            throw new RuntimeException("Aucun fichier sélectionné");
        }

        if(file.getSize()>maxSize){
            throw  new RuntimeException("Fichier trop volumineux (max 5MB)");
        }

        String contentType = file.getContentType();

        if(!allowedTypes.contains(contentType)){
            throw new RuntimeException("Format non autorisé. Utilisez JPEG, PNG ou GIF");
        }
    }



    // Extraire l'extension du fichier
    private String getFileExtension(String filename){
        if(filename==null || filename.isEmpty()){
            return "";
        }
        int dernierPoint = filename.lastIndexOf('.');
        if(dernierPoint==-1){
            return "";
        }
        return filename.substring(dernierPoint);
    }


    public boolean deleteProfilePicture(String filePath) throws IOException{
        try{
            if(filePath!=null  &&  !filePath.isEmpty()){
                Path path=Paths.get(uploadDir,filePath);
                return Files.deleteIfExists(path);
            }
        }catch (IOException e){
            System.err.println("Erreur lors de la suppression du fichier : "+e.getMessage());
        }
        return false;
    }


    /**
     * Générer l'URL publique pour accéder à l'image
     * @param filePath : le chemin du fichier
     * @return l'URL complète
     */
    public String createImageUrl(String filePath){
        if(filePath==null || filePath.isEmpty()){
            return null;
        }
        return "/api/files/" + filePath;
    }

}
