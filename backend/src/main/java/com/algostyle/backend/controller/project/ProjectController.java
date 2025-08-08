package com.algostyle.backend.controller.project;

import com.algostyle.backend.model.entity.Project;
import com.algostyle.backend.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:8080")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<Project> getAllProjects(){
        return this.projectService.findAll();
    }

    @PostMapping
    public Project createProject(@RequestBody Project project){
        return  this.projectService.save(project);
    }

    @PostMapping("/upload")
    public Project createProjectsWithFiles(
            @RequestParam("projet") String projetJson,
            @RequestParam(value = "file", required=false) MultipartFile file,
            @RequestParam(value = "image", required = false) MultipartFile image
            ){
        try{
            ObjectMapper mapper=new ObjectMapper();
            Project project=mapper.readValue(projetJson,Project.class);
            return projectService.saveProjectWithFiles(project,file,image);
        }catch (Exception e){
            throw new RuntimeException("Erreur lors de la création du projet "+e);
        }
    }


    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id){
        return this.projectService.findById(id);
    }

}
