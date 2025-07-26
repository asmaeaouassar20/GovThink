import { Component, inject, OnInit } from '@angular/core';
import { SidebarComponent } from "../../../sidebars/sidebar/sidebar.component";
import {  RouterLink, RouterOutlet } from '@angular/router';
import { DashboardService } from '../../../service/dashboard.service';

@Component({
  selector: 'app-dashboard',
  imports: [SidebarComponent,RouterOutlet,RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent  {
 


}
