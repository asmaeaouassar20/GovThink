import { Component, ElementRef, ViewChild } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { NgFor, NgIf } from '@angular/common';
import { Project } from '../../interfaces/projects';
import { HttpClient } from '@angular/common/http';
import { ProjectService } from '../../service/project.service';

@Component({
  selector: 'app-users-projects',
  imports: [SidebarComponent, NgFor, NgIf],
  templateUrl: './users-projects.component.html',
  styleUrl: './users-projects.component.css'
})
export class UsersProjectsComponent {


  addingNewProject=false; // Pour l'ajout d'un nouveau projet
  projects : Project[] = [];
  newProject : Project = { titre:'', description:'', lienProjet:''};
  selectedFile : File | null = null;
  selectedImage : File | null = null;



  constructor(private http:HttpClient, private projectService:ProjectService){
    this.loadProjects();
  }



  loadProjects(){
    this.projectService.getAllProjects().subscribe(data => this.projects=data);
  }




  onFileSelected(event : any){
    this.selectedFile=event.target.files[0];
    console.log("vous avez sélectionné un fichier");
  }
  onImageSelected(event : any){
    this.selectedImage=event.target.files[0];
    console.log("vous avez sélectionné une image")
  }


  publishProject(){
    if(this.newProject.titre && this.newProject.description && this.newProject.lienProjet){
      const formData = new FormData();
      formData.append('projet',JSON.stringify({
        titre : this.newProject.titre,
        description : this.newProject.description,
        lienProjet : this.newProject.lienProjet
      }));
      if(this.selectedFile){
        formData.append('file', this.selectedFile);
      }
      if(this.selectedImage){
        formData.append('image', this.selectedImage);
      }

      this.projectService.publishProject(formData).subscribe(project => {
        this.projects.push(project);
      });
    }
  }


}
