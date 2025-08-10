import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { NgFor, NgIf } from '@angular/common';
import { Project } from '../../interfaces/projects';
import { HttpClient } from '@angular/common/http';
import { ProjectService } from '../../service/project.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-users-projects',
  imports: [SidebarComponent, NgFor, NgIf, FormsModule, ReactiveFormsModule],
  templateUrl: './users-projects.component.html',
  styleUrl: './users-projects.component.css'
})
export class UsersProjectsComponent implements OnInit{


  addingNewProject=false; // Pour l'ajout d'un nouveau projet
  projects : Project[] = [];
  selectedFile : File | null = null;
  selectedImage : File | null = null;
  myForm : FormGroup;



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
      },
      error : (error) =>{
        console.error('Erreur lors du chargement du projet');
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


}
