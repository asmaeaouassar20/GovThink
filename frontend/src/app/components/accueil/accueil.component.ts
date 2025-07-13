import { Component, OnInit , ViewChild, ElementRef, OnDestroy} from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";


import { Chart, ChartConfiguration, ChartType } from 'chart.js';
import { registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-accueil',
  imports: [SidebarComponent],
  templateUrl: './accueil.component.html',
  styleUrl: './accueil.component.css'
})
export class AccueilComponent implements OnInit, OnDestroy {
  @ViewChild('donutCanvas', { static: true }) donutCanvas!: ElementRef<HTMLCanvasElement>;
  
  private chart: Chart | undefined;

  ngOnInit(): void {
    this.createDonutChart();
  }

  private createDonutChart(): void {
    const ctx = this.donutCanvas.nativeElement.getContext('2d');
    
    if (!ctx) {
      console.error('Impossible d\'obtenir le contexte du canvas');
      return;
    }

    // Configuration simplifiée du diagramme en anneau
    this.chart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        datasets: [{
          data: [3, 97],
          backgroundColor: [
            '#739CC0 ',
            '#fff'
          ],
          borderColor: '#ffffff',
          borderWidth: 0,
          hoverOffset: 5
        }]
      },
      options: {
        responsive: true,
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

  // Méthode pour mettre à jour les données
  

  ngOnDestroy(): void {
    if (this.chart) {
      this.chart.destroy();
    }
  }
}