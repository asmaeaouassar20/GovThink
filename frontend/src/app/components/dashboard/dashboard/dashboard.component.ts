import { Component, inject, OnInit } from '@angular/core';
import { SidebarComponent } from "../../../sidebars/sidebar/sidebar.component";
import { RouterLink, RouterOutlet } from '@angular/router';
import { DashboardService } from '../../../service/dashboard.service';
import { error } from 'console';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [SidebarComponent, RouterLink,RouterOutlet,NgFor],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  files : any[] = [];


  dashboardService = inject(DashboardService);


  ngOnInit(): void {
      this.lireFichers();
  }


  lireFichers(){
    this.dashboardService.getAllFiles().subscribe({

      next : (res)=>{
        this.files=res;
      },

      error : (error)=>{
        console.error("Erreur dans la récupération des fichiers");
      }
    })
  }
}
