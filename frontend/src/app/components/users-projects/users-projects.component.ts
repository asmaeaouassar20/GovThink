import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import {NgIf } from '@angular/common';
import { Project } from '../../interfaces/projects';
import { HttpClient } from '@angular/common/http';
import { ProjectService } from '../../service/project.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TruncatePipe } from '../../pipes/truncate.pipe';
 
@Component({
  selector: 'app-users-projects',
  imports: [SidebarComponent, NgIf, FormsModule, ReactiveFormsModule, TruncatePipe],
  templateUrl: './users-projects.component.html',
  styleUrl: './users-projects.component.css'
})
export class UsersProjectsComponent implements OnInit{


  addingNewProject=false; // Pour l'ajout d'un nouveau projet
  projects : Project[] = [];
  selectedFile : File | null = null;
  selectedImage : File | null = null;
  myForm : FormGroup;
  selectedProject : Project;
  filteredProjects : Project[] = [];


  // Pour la recherche
  searchedTerm : string = '';  // le mot recherché




  constructor(private http:HttpClient,
              private projectService:ProjectService,
              private fb : FormBuilder
              ){
    this.initForm();
  }

  ngOnInit(): void {
      this.loadProjects();
  }

  initForm(){
  this.myForm = this.fb.group({
      titre : ['', [Validators.required]],
      description : ['', [Validators.required]],
      lienProjet : ['', Validators.required], // pour le fichier projet
    });
  }



  loadProjects(){
    this.projectService.getAllProjects().subscribe({
      next : (data) => {
        this.projects=data;
        this.filteredProjects=[...this.projects];
      },
      error : (error) =>{
        console.error('Erreur lors du chargement du projet');
        console.log(error);
      }
    });
  }

  



  publishProject(){
    if(this.myForm.valid){
      const formData = new FormData();
      formData.append('projet',JSON.stringify({
        titre : this.myForm.value.titre,
        description : this.myForm.value.description,
        lienProjet : this.myForm.value.lienProjet
      }));

      if(this.selectedFile){
        formData.append('file', this.selectedFile);
      }
      if(this.selectedImage){
        formData.append('image', this.selectedImage);
      }
      
    
      this.projectService.publishProject(formData).subscribe(project => {
        this.projects.push(project);
        setTimeout(()=>{
          this.addingNewProject=false;
          this.resetMyForm();
          this.loadProjects();
        },2000)
      });
    }
  }

  onFileSelected(event : Event, field:string){
    const input = event.target as HTMLInputElement;
    if(input.files && input.files.length>0){
      if(field==='image'){
        this.selectedImage=input.files[0];
      }else{
        this.selectedFile=input.files[0];
      }
    }
  }


  resetMyForm(){
    this.myForm.reset();
    this.selectedFile=null;
    this.selectedImage=null;

    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => (input as HTMLInputElement).value='');
  }







  // sélectionner un projet pour avoir plus de ses détails
  selectProject(project : Project){
    this.selectedProject=project;
  }



  filterProjects(){
    if(!this.searchedTerm || !this.searchedTerm.trim()){
      this.filteredProjects=[...this.projects];
      return;
    }

    // le mot recherché
    const keyWord = this.searchedTerm.toLowerCase().trim();

    this.filteredProjects = this.projects.filter( project => {

      // recherche dans le  titre
      const titleMatch = project.titre && project.titre.toLowerCase().includes(keyWord);

      // recherche dans la description
      const descriptionMatch = project.description && project.description.toLowerCase().includes(keyWord);

      // recherche dans imageUrl
      const imageUrlMatch = project.imageUrl && project.imageUrl.toLowerCase().includes(keyWord);

      // recherche dans lienProjet
      const lienProjetMatch = project.lienProjet && project.lienProjet.toLowerCase().includes(keyWord);

      // recherche dans fileUrl
      const fileUrl=project.fileUrl && project.fileUrl.toLowerCase().includes(keyWord);

      return titleMatch || descriptionMatch || imageUrlMatch || lienProjetMatch || fileUrl

    })
  }
  
onSearch(event:any){
  this.searchedTerm=event.target.value;
  this.filterProjects();
}

}
