import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../../../service/dashboard.service';
import { NgIf, NgFor } from '@angular/common';

@Component({
  selector: 'app-tables',
  imports: [NgFor, NgIf],
  templateUrl: './tables.component.html',
  styleUrl: './tables.component.css'
})
export class TablesComponent implements OnInit {

  data : any[] = [];    // Tableau qui contiendra toutes les lignes de données du fichiers Excel
  columns : string[] = [];  // Tableau pour stocker les noms des colonnes  ex: [ "Nom" , "Age" , "Ville" ]
  loading = false;    // indicateur d'état de chargement, utilisé pour afficher un spinner ou bloquer les actions pendant le chargement
  myId:number = -1  // Variable pour stocker l'ID du fichier à lire
  files: any[] = [];

  // Injection du service DashboardService pour faire l'appel HTTP au backend
  constructor(private dashboardService : DashboardService){}
 

  // Méthode exécutée automatiquement au moment où le composant est initialisé
  ngOnInit(): void {
    this.lireFichier(this.myId);
    // this.getFiles();
  }


  // Méthode pour lire un fichier Excel à partir de son identifiant (fileId)
  lireFichier(fileId : number){
    this.loading = true;  // Activer l'indicateur de chargement

    // Appel du service qui va chercher les donnée sExcel depuis l'API
    this.dashboardService.getExcelData(fileId).subscribe({

      // En cas de succès
      next : (data) =>{
        this.data= data;  // On stocke les donnéées Excel dans la variable 'data'

        if(data.length>0){  // Si le tableau n'est pas vide, on extrait les noms des colonnes
          this.columns = Object.keys(data[0]);   // On récupère les clés du header qui représente les entêtes de colonnes
        }
        this.loading = false;   // désactiver le chargement
      },

      error : (err) => {
        console.log('Error loading data', err);
        this.loading =false;
      }

    });
  }







  getFiles(){
    this.dashboardService.getAllFiles().subscribe(res => {
      this.files = res;
    })
  }


}
