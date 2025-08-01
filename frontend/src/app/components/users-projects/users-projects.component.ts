import { Component, ElementRef, ViewChild } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-users-projects',
  imports: [SidebarComponent, NgFor],
  templateUrl: './users-projects.component.html',
  styleUrl: './users-projects.component.css'
})
export class UsersProjectsComponent {

  @ViewChild('addNewProjectModal') addNewProjectModal : ElementRef | undefined;

  L = [1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8,
    1,2,3,4,5,6,7,8
  ];


  openModalAddProject(){
    if(this.addNewProjectModal)
      this.addNewProjectModal.nativeElement.style.display = 'block';
  }

  closeModalAddProject(){
    if(this.addNewProjectModal){
      this.addNewProjectModal.nativeElement.style.display='none';
    }
  }
}
