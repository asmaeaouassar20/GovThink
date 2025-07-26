import { Component, ElementRef, ViewChild, AfterViewInit, OnDestroy, ChangeDetectorRef, OnInit } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Chart, ChartType, registerables } from 'chart.js';
import { DashboardService } from '../../../../service/dashboard.service';



interface ColumnAnalysis {
  name: string;                       // Nom de la colonne
  type: 'text' | 'number' | 'mixed';  // Type de données détecté
  uniqueValues: number;               // Nombre de valeurs uniques
  totalValues: number;                // Nombre total de valeurs non vides
  sampleValues: string[];             // Exemples de valeurs (5 premières)
  isGoodForVisualization: boolean;    // Si la colonne est adaptée à un graphique
}



@Component({
  selector: 'app-charts',
  imports: [FormsModule, NgIf, NgFor],
  templateUrl: './charts.component.html',
  styleUrl: './charts.component.css'
})
export class ChartsComponent implements AfterViewInit, OnDestroy, OnInit {

  
  // Référence au canvas HTML pour Chart.js
  @ViewChild('chartCanvas', { static: true }) chartCanvas!: ElementRef<HTMLCanvasElement>;


  data: any[] = []; // Données brutes du fichier Excel
  rawColumns: string[] = [];  // Noms des colonnes brutes
  analyzedColumns: ColumnAnalysis[] = []; // Colonnes analysées
  loading = false;  // Etat de chargement
  chart: Chart | undefined; // Instance du graphique Chart.js
  chartType: ChartType = 'bar'; // type du graphique ( 'bar' par défaut )
  files :   any[]=[];  // Liste des fichiers disponibles
  myId : number = -1;
  currentFileName : string ='';
  currentFileLink : string ='';


  // Colonnes sélectionnées pour la visualisation
  selectedColumn = '';
  visualizableColumns: ColumnAnalysis[] = [];


  constructor(
    private dashboardService: DashboardService, // Service pour les appels API
    private cdr: ChangeDetectorRef  // Pour forcer la détection de changement
  ) {
    Chart.register(...registerables); // Enregistrement des plugins Chart.js
  }


  ngOnInit(){
    console.log("-- ngOnInit --")
    this.getFiles();
  }

 ngAfterViewInit() {
// Affichage dans la console du navigateur une fois que la vue est prête
// pour vérifier que le composant est bien rendu
    console.log('-- Component initialisé --');
  }




  getFiles(){
      this.dashboardService.getAllFiles().subscribe(res=>{
      this.files=res;
    })
  }


  getFileById(fileId : number){
      this.dashboardService.getFileById(fileId).subscribe({
        next : (res) =>{
          this.currentFileName=res.nom;
          this.currentFileLink=res.networkLink;
        },
  
        error : (error) =>{
          console.error("fichier non trouvé avec id "+fileId);
        }
      })
    }



  lireFichier(fileId: number) {
    this.getFileById(fileId);
    this.myId=fileId;
    this.loading = true;
    this.resetData(); // Réinitialisation des données précédentes

    this.dashboardService.getExcelData(fileId).subscribe({
      next: (data) => {
        console.log('Données reçues:', data);
        this.data = data;

        if (data.length > 0) {
          this.rawColumns = Object.keys(data[0]); // Extraction des noms de colonnes
          this.analyzeColumns();  // Analyse des colonnes
          this.selectBestColumnForVisualization();  // Sélectionner les colonnes qui peuvent être visualisées

          this.cdr.detectChanges(); // Mise à jour de la vue

          // Double requestAnimationFrame pour s'assurer que la vue est mise à jour
          requestAnimationFrame(() => {
            requestAnimationFrame(() => {
              if (this.selectedColumn) {
                this.generateChartData(); // Génération du graphique
              }
            });
          });
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement:', err);
        this.loading = false;
      }
    });
  }







  private resetData() {
    this.data = [];
    this.rawColumns = [];
    this.analyzedColumns = [];
    this.visualizableColumns = [];
    this.selectedColumn = '';
    if (this.chart) {
      this.chart.destroy();   // Nettoyage du graphiqye existant
      this.chart = undefined;
    }
  }






private analyzeColumns() {
    this.analyzedColumns = this.rawColumns.map(columnName => {
      const analysis = this.analyzeColumn(columnName);
      console.log(`Analyse de "${columnName}":`, analysis);
      return analysis;
    });

    // Filtrer les colonnes bonnes pour la visualisation
    this.visualizableColumns = this.analyzedColumns.filter(col => col.isGoodForVisualization);

    console.log('Colonnes visualisables:', this.visualizableColumns.map(c => c.name));
  }





  private analyzeColumn(columnName: string): ColumnAnalysis {

    // Extraction et nettoyage des valeurs
    const values = this.data.map(row => row[columnName])
                           .filter(val => val !== null && val !== undefined && val !== '');

    const uniqueValues = [...new Set(values)];  // Les valeurs uniques
    const totalValues = values.length;
    const uniqueCount = uniqueValues.length;

    // Échantillon des premières valeurs uniques (max 5)
    // Utilisé dans l'UI pour donner un aperçu à l'utilisateur
    const sampleValues = uniqueValues.slice(0, 5).map(v => String(v));

    // Déterminer le type de données
    // 80% de numériques = colonne numérique
    const numericValues = values.filter(val => !isNaN(Number(val)) && val !== '');  // retirer les chaînes vides, convertir les autres en nombres, et ne garder que celles qui donnent des nombres valides
    const isNumeric = numericValues.length > totalValues * 0.8; // 80% sont numériques

    let type: 'text' | 'number' | 'mixed';
    if (isNumeric) {  // cas 1 : >80% de nombres
      type = 'number';
    } else if (numericValues.length === 0) {  // Cas 2 : 0 nombre détecté
      type = 'text';
    } else {  // Cas 3 : Entre 1% et 79% de nombres
      type = 'mixed';
    }

    // Critères pour déterminer si c'est bon pour la visualisation
    const duplicateRatio = (totalValues - uniqueCount) / totalValues;
    const isGoodForVisualization = 
      uniqueCount > 1 && // Au moins 2 valeurs différentes
      uniqueCount < totalValues * 0.8 && // Pas trop unique (max 80% unique)
      duplicateRatio > 0.1 && // Au moins 10% de doublons
      uniqueCount <= 15; // Maximum 15 catégories différentes pour la lisibilité

    return {
      name: columnName,
      type,
      uniqueValues: uniqueCount,
      totalValues,
      sampleValues,
      isGoodForVisualization
    };
  }




  private selectBestColumnForVisualization() {
    if (this.visualizableColumns.length === 0) {
      console.log('Aucune colonne appropriée pour la visualisation');
      return;
    }

    // Trier par ordre de préférence :
    // 1. Moins de valeurs uniques (plus de regroupement)
    // 2. Plus de valeurs totales
    // 3. Type texte de préférence
    const sorted = this.visualizableColumns.sort((a, b) => {
      // Priorité 1: moins de valeurs uniques
      if (a.uniqueValues !== b.uniqueValues) {
        return a.uniqueValues - b.uniqueValues;
      }

      // Priorité 2: plus de données
      if (a.totalValues !== b.totalValues) {
        return b.totalValues - a.totalValues;
      }

      // Priorité 3: préférer text > mixed > number pour les catégories
      const typeOrder = { 'text': 0, 'mixed': 1, 'number': 2 };
      return typeOrder[a.type] - typeOrder[b.type];
    });

    this.selectedColumn = sorted[0].name;
    console.log('Colonne sélectionnée automatiquement:', this.selectedColumn);
  }






    // Générer le graphique avec Chart.js avec la colonne sélectionnée
  generateChartData() {
    if (!this.chartCanvas?.nativeElement || !this.selectedColumn || this.data.length === 0) {
      console.log('Conditions non remplies pour générer le graphique');
      return;
    }

    // Comptage des occurrences par valeur
    const counts: { [key: string]: number } = {};
    let processedValues = 0;

    this.data.forEach(row => {
      const value = row[this.selectedColumn];
      if (value !== undefined && value !== null && value !== '') {
        const cleanValue = String(value).trim();
        if (cleanValue) {
          counts[cleanValue] = (counts[cleanValue] || 0) + 1;
          processedValues++;
        }
      }
    });

    console.log(`Comptages pour "${this.selectedColumn}":`, counts);
    console.log(`Valeurs traitées: ${processedValues} sur ${this.data.length}`);

    // Trier par nombre d'occurrences (descendant)
    const sortedEntries = Object.entries(counts).sort((a, b) => b[1] - a[1]);
    const labels = sortedEntries.map(entry => entry[0]);
    const data = sortedEntries.map(entry => entry[1]);

    // Nettoyage du graphique précédent
    if (this.chart) {
      this.chart.destroy();
    }

    // Adapter le type de graphique selon le nombre de catégories
    let recommendedType = this.chartType;
    if (labels.length > 10 && (this.chartType === 'pie' || this.chartType === 'doughnut')) {
      recommendedType = 'bar'; // Trop de catégories pour un graphique en secteurs / Les secteurs deviennent illisibles
    }

    // Création du graphique
    this.chart = new Chart(this.chartCanvas.nativeElement, {
      type: recommendedType,
      data: {
        labels: labels,
        datasets: [{
          label: `Répartition par ${this.selectedColumn.trim()}`,
          data: data,
          backgroundColor: this.generateColors(labels.length),
          borderColor: this.generateBorderColors(labels.length),
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: labels.length > 8 ? 'right' : 'top',
            display: labels.length <= 15 // Cacher la légende si trop de catégories
          },
          title: {
            display: true,
            text: `Répartition par ${this.selectedColumn.trim()} (${processedValues} éléments)`
          }
        },

        // Configuration des axes pour les graphiques non-secteurs
        scales: recommendedType === 'pie' || recommendedType === 'doughnut' ? {} : {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1
            }
          },
          x: {
            ticks: {
              maxRotation: labels.some(l => l.length > 10) ? 45 : 0,
              minRotation: 0
            }
          }
        }
      }
    });

    console.log(`Graphique créé (${recommendedType}) pour: ${this.selectedColumn}`);
  }



  // Générer un tableau de couleurs pour les données
  generateColors(count: number): string[] {
    // Couleurs de base Chart.js
    const baseColors = [
     'rgba(67, 56, 202, 0.8)', 'rgba(16, 185, 129, 0.8)', 'rgba(245, 101, 101, 0.8)',
'rgba(251, 191, 36, 0.8)', 'rgba(139, 69, 19, 0.8)', 'rgba(75, 85, 99, 0.8)',
'rgba(219, 39, 119, 0.8)', 'rgba(34, 197, 94, 0.8)', 'rgba(249, 115, 22, 0.8)',
'rgba(168, 85, 247, 0.8)', 'rgba(20, 184, 166, 0.8)', 'rgba(220, 38, 127, 0.8)'
    ];

    const colors = [];
    for (let i = 0; i < count; i++) {
      if (i < baseColors.length) {
        colors.push(baseColors[i]);
      } else {
        // Générer des couleurs aléatoires pour les catégories supplémentaires
        const hue = (i * 137.5) % 360; // Distribution dorée - Angle doré pour la répartition
        colors.push(`hsla(${hue}, 70%, 60%, 0.8)`);
      }
    }
    return colors;
  }


  // Générer des couleurs de bordure
  // En modifiant l'opacité alpha (la transparence) d'un ensemble de couleurs.
  generateBorderColors(count: number): string[] {
    return this.generateColors(count).map(color => color.replace('0.8)', '1)'));
  }






  // - Handler de changement de colonne sélectionnée
  // - Cette méthode est appelée quand l'utilisateur change la colonne sélectionnée
  onColumnChange() {
    if (this.selectedColumn && this.data.length > 0) {
      this.generateChartData();
    }
  }



  // Changer le type de graphique
  changeChartType(type: ChartType) {
    this.chartType = type;
    if (this.selectedColumn && this.data.length > 0) {
      this.generateChartData();
    }
  }



  // Obtenir des recommandations pour l'utilisateur
  // Donne un conseil lisible sur l'utilité d'une colonne
  getColumnRecommendation(column: ColumnAnalysis): string {
    if (!column.isGoodForVisualization) {
      if (column.uniqueValues === column.totalValues) {
        return 'Trop unique (chaque valeur est différente)';
      }
      if (column.uniqueValues > 15) {
        return 'Trop de catégories différentes';
      }
      if (column.uniqueValues === 1) {
        return 'Une seule valeur unique';
      }
      return 'Non recommandé pour la visualisation';
    }

    const duplicateRatio = (column.totalValues - column.uniqueValues) / column.totalValues;
    if (duplicateRatio > 0.7) {
      return '✔️ Excellent pour la visualisation';
    } else if (duplicateRatio > 0.4) {
      return '✔️ Bon pour la visualisation';
    } else {
      return '⚠️ Acceptable pour la visualisation';
    }
  }



 ngOnDestroy() {
    if (this.chart) {
      this.chart.destroy();
    }
  }




}
