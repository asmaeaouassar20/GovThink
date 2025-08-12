package com.algostyle.backend.controller.project;


import com.algostyle.backend.model.entity.Project;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController // indique que cette classe est un contrôleur REST qui renvoie des données JSON
@RequestMapping("/api/projects")    // toutes les routes commencent par /api/projects
@CrossOrigin(origins = "http://localhost:4200") // Autorise le frontend Angular (port 4200) à accéder à cette API
public class ProjectController {


    // Injection automatique du service qui gère la logique métier
    @Autowired
    private ProjectService projectService;


    /**
     * Endpoint POST pour créer un projet avec éventuellement un fichier et une image
     * URL : POST /api/projects/upload
     * @param projetJson JSON représentant un projet
     * @param file  fichier optionnel associé au projet
     * @param image image optionnelle du projet
     * @return  le projet sauvegardé
     */
    @PostMapping("/upload")
    public Project createProjectsWithFiles(
            @RequestParam("projet") String projetJson,      // Projet en format JSON
            @RequestParam(value = "file", required = false) MultipartFile file,    // Fichier attaché (PDF, doc ...)
            @RequestParam(value = "image", required = false) MultipartFile image,    // Image attachée (PNG, JPG ... )
            @AuthenticationPrincipal User user
            ){
        try{
            // Conversion du JSON reçu en objet Java Projet
            ObjectMapper mapper = new ObjectMapper();
            Project project = mapper.readValue(projetJson, Project.class);
            project.setUser(user);

            // Sauvegarder le projet avec les fichiers via le service
            Project savedProject = projectService.saveProjectWithFiles(project, file, image);

            // Construire l'URL complète de l'image
            if(savedProject.getImageUrl() != null){
                String imagePath = savedProject.getImageUrl();

                // Supprimer les préfixes "/uploads/" s'ils existent
                imagePath = imagePath.replaceFirst("^/?uploads/","");
                savedProject.setImageUrl("http://localhost:8080/uploads/"+imagePath);
            }


            // Construire correctement l'URL complète du fichier
            if(savedProject.getFileUrl() != null){
                String filePath = savedProject.getFileUrl();
                filePath = filePath.replaceFirst("^/?uploads/","");
                savedProject.setFileUrl("http://localhost:8080/uploads/"+filePath);
            }

            return savedProject; // Retourner le projet enregistré avec ses URLs complètes
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de la création du projet : "+e.getMessage());
        }
    }




    @GetMapping
    public List<Project> getAllProjects(){

        // Récupérer tous les projets depuis le service
        List<Project> projects = this.projectService.findAll();

        // Correction des URLs des fichiers et images pour chaque projet
        projects.forEach( project -> {
            if(project.getImageUrl()!=null){
                String imagePath = project.getImageUrl();
                imagePath = imagePath.replaceFirst("^/?uploads/","");
                project.setImageUrl("http://localhost:8080/uploads/"+imagePath);
            }

            if(project.getFileUrl()!=null){
                String filePath = project.getFileUrl();
                filePath = filePath.replaceFirst("^/?uploads/","");
                project.setFileUrl("http://localhost:8080/uploads/"+filePath);
            }
        });

        return projects;
    }



}
