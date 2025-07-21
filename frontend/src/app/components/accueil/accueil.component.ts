import { Component, OnInit , ViewChild, ElementRef, OnDestroy} from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";


// Importer les types et fonctions de Chart.js pour la création de graphiques
import { Chart, ChartConfiguration, ChartType } from 'chart.js';
import { registerables } from 'chart.js';
import { UserService } from '../../service/user.service';
import { Router } from '@angular/router';


// Enregistrement des composants nécessaires de Chart.js
Chart.register(...registerables);

@Component({
  selector: 'app-accueil',
  imports: [SidebarComponent],
  templateUrl: './accueil.component.html',
  styleUrl: './accueil.component.css'
})
export class AccueilComponent implements OnInit, OnDestroy {
  
  // Référence à l'élément canvas dans le template via ViewChild
  @ViewChild('donutCanvas', { static: true }) donutCanvas!: ElementRef<HTMLCanvasElement>;
  
  private chart: Chart | undefined;
  connectedUser : any = null;


  constructor(private userService : UserService,
            private router : Router
  ){}


  ngOnInit(): void {
    this.createDonutChart();
  }



  

  // Charger les données de l'utilisateur
  loadProfile() : void{
    this.userService.getProfile().subscribe({
      next : (response) => {
        this.connectedUser=response;
      },
      error : (error) => {
        console.error('Ereur lors du chargement du profile');
      }
    });
  }













  // Création du graphique en anneau
  private createDonutChart(): void {
    const ctx = this.donutCanvas.nativeElement.getContext('2d');
    
    if (!ctx) {
      console.error('Impossible d\'obtenir le contexte du canvas');
      return;
    }

    // Configuration simplifiée du diagramme en anneau
    this.chart = new Chart(ctx, {
      type: 'doughnut',  // Type du graphique : en anneau
      data: {
        datasets: [{
          data: [3, 97],  // Donnée du graphique : 3% et 97%
          backgroundColor: [
            '#739CC0 ', // Couleur pour la 1ère partie 3%
            '#fff'  // Couleur pour la 2ème partie 97%
          ],
          borderColor: '#ffffff',
          borderWidth: 0,
          hoverOffset: 5  //Décalage au survol
        }]
      },
      options: {
        responsive: true, // Le graphique s'adapte à la taulle du conteneur
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              padding: 0,
              font: {
                size: 0
              }
            }
          },
          tooltip: {
            backgroundColor: 'rgba(0, 0, 0, 0.8)',
            titleColor: '#ffffff',
            bodyColor: '#ffffff',
            callbacks: {
              label: (context: any) => {
                const label = context.label || '';
                const value = context.parsed;
                return `${label}: ${value}%`;
              }
            }
          }
        },
        animation: {
          duration: 1000
        }
      }
    });
  }


  /**
   * Méthode de cycle de vie Angular - appelée avant la description du composant
   * - Permet de nettoyer les ressources (destruction du graphique)
   */
  ngOnDestroy(): void {
    if (this.chart) {
      this.chart.destroy(); // Desctruction propre du graphique pour éviter les fuites mémoire
    }
  }
}