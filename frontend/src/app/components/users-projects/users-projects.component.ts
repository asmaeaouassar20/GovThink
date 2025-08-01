import { Component } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-users-projects',
  imports: [SidebarComponent, NgFor],
  templateUrl: './users-projects.component.html',
  styleUrl: './users-projects.component.css'
})
export class UsersProjectsComponent {

  L = [1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8
  ];
}
