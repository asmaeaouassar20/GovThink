import { Component, OnInit} from '@angular/core';
import { RouterLink} from '@angular/router';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";


// Importer les types et fonctions de Chart.js pour la création de graphiques
import { UserService } from '../../service/user.service';
import { PostService } from '../../service/post.service';
import { ProjectService } from '../../service/project.service';
import { DashboardService } from '../../service/dashboard.service';


@Component({
  selector: 'app-accueil',
  imports: [SidebarComponent,RouterLink],
  templateUrl: './accueil.component.html',
  styleUrl: './accueil.component.css'
})
export class AccueilComponent implements OnInit {
  
  username : string = 'someone';


  // Les contributions de l'utilisateur connecté
  nbrPublishedPosts : number =0;
  nbrMyProjets : number =0;

  // Total
  nbrTotalPosts : number = 0; // Nombre total des posts
  nbrTotalprojets : number = 0; // Nombre total des projets
  nbrTotalDatasets : number =0; // Nombre total de datasets


  constructor(private userService : UserService,
            private postService : PostService,
            private projectService : ProjectService,
            private dashboardService : DashboardService
  ){}


  ngOnInit(): void {
    this.loadProfile();
    this.getNumberOfPublishedPosts();
    this.getNumberOfMyProjects();
    this.getNombreTotalPosts();
    this.getNombreTotalProjets();
    this.getNombreTotalDatasets();
  }



  

  // Charger les données de l'utilisateur
  loadProfile() : void{
    this.userService.getProfile().subscribe({
      next : (response) => {
        if(response.prenom!==null) this.username=response.prenom;
        else if (response.nom!==null) this.username=response.nom;
        else {
          if(response.email.inculdes('@')){
            this.username=response.email;
          }else{
            alert("attention:  format de l'email incorrect");
            this.username=null;
          }
        }
      },
      error : (error) => {
        console.error('Ereur lors du chargement du profile');
      }
    });
  }









  getNumberOfPublishedPosts(){
    this.postService.getPublishedPosts().subscribe({
      next : (posts) => {
        this.nbrPublishedPosts=posts.length;
        console.log("nbrPublishedPosts : ",this.nbrPublishedPosts);
      },
      error : (erreur) => {
        console.log("Erreur lors de la récupération des postes publiés");
        console.log(erreur);
      }
    })
  }

  getNumberOfMyProjects(){
    this.projectService.getAllMyProjects().subscribe({
      next : (projects) =>{
         this.nbrMyProjets=projects.length;
         console.log("nbrMyProjets : ",this.nbrMyProjets);
      },
      error : (erreur) => {
        console.log("Erreur lors de la récupération de mes projets");
        console.log(erreur);
      }
    })
  }


  getNombreTotalPosts(){
    this.postService.getAllPosts().subscribe({
      next : (posts) => this.nbrTotalPosts=posts.length,
      error : (erreur) => {
        console.log("Erreur lors de la récupération du nombre total des posts");
        console.log(erreur);
      }
    })
  }


  getNombreTotalProjets(){
    this.projectService.getAllProjects().subscribe({
      next : (projects) => {
        this.nbrTotalprojets=projects.length
      },
      error : (erreur) => {
        console.log("Erreur lors de la récupération de tous les projets");
        console.log(erreur);
      }
    })
  }



  getNombreTotalDatasets(){
    this.dashboardService.getAllFiles().subscribe({
      next : (files) => this.nbrTotalDatasets=files.length,
      error : (erreur) => {
        console.log("Erreur lors de la récupération des datasets. ERREUR: ");
        console.log(erreur);
      }
    })
  }
}