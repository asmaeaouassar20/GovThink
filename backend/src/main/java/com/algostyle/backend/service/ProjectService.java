package com.algostyle.backend.service;


import com.algostyle.backend.model.entity.Project;
import com.algostyle.backend.model.entity.User;
import com.algostyle.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    private final String UPLOAD_DIR = "uploads/";

    public List<Project> findAll(){
        return this.projectRepository.findAll();
    }
    public Project save(Project project){
        return this.projectRepository.save(project);
    }
    public Project findById(Long id){
        return this.projectRepository.findById(id).orElseThrow(()->new RuntimeException("Projet avec id "+id+" nexste pas"));
    }


    public List<Project> getProjectsByUser(User user){
        return this.projectRepository.findAllByUser(user);
    }

    public Project saveProjectWithFiles(
            Project project,
            MultipartFile file,
            MultipartFile image
    ){
        try{
            // Créer le dossier s'il n'existe pas
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            // gérer le fichier
            if(file!=null && !file.isEmpty()){
                String filename = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), filePath);
                project.setFileUrl("/uploads/"+filename);
            }

            // gérer l'image
            if(image!=null && !image.isEmpty()){
                String imageName = UUID.randomUUID().toString()+"_"+image.getOriginalFilename();
                Path imagePath = uploadPath.resolve(imageName);
                Files.copy(image.getInputStream(), imagePath);
                project.setImageUrl("/uploads/"+imageName);
            }

            return this.projectRepository.save(project);
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de l'upload des fichiers");
        }
    }
}
