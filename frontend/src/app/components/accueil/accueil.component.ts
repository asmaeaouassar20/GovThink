import { Component, OnInit , ViewChild, ElementRef, OnDestroy} from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";


// Importer les types et fonctions de Chart.js pour la création de graphiques
import { UserService } from '../../service/user.service';
import { PostService } from '../../service/post.service';


@Component({
  selector: 'app-accueil',
  imports: [SidebarComponent],
  templateUrl: './accueil.component.html',
  styleUrl: './accueil.component.css'
})
export class AccueilComponent implements OnInit {
  
  // Référence à l'élément canvas dans le template via ViewChild
  @ViewChild('donutCanvas', { static: true }) donutCanvas!: ElementRef<HTMLCanvasElement>;
  
  prenom : string = 'someone';


  // Les contributions de l'utilisateur connecté
  nbrDatasets : number =0;
  nbrPublishedPosts : number =0;
  nbrProjets : number =0;


  constructor(private userService : UserService,
            private postService : PostService,
  ){}


  ngOnInit(): void {
    this.loadProfile();
    this.getNumberOfPublishedPosts();
  }



  

  // Charger les données de l'utilisateur
  loadProfile() : void{
    this.userService.getProfile().subscribe({
      next : (response) => {
        this.prenom=response.prenom;
      },
      error : (error) => {
        console.error('Ereur lors du chargement du profile');
      }
    });
  }









  getNumberOfPublishedPosts(){
    this.postService.getPublishedPosts().subscribe({
      next : (posts) => this.nbrPublishedPosts=posts.length,
      error : (erreur) => {
        console.log("Erreur lors de la récupération des postes publiés");
        console.log(erreur);
      }
    })
  }


}