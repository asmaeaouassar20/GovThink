import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from "../../sidebars/sidebar/sidebar.component";
import { Project } from '../../interfaces/projects';
import { ProjectService } from '../../service/project.service';
import { TruncatePipe } from '../../pipes/truncate.pipe';

@Component({
  selector: 'app-my-projects',
  imports: [SidebarComponent, TruncatePipe],
  templateUrl: './my-projects.component.html',
  styleUrl: './my-projects.component.css'
})
export class MyProjectsComponent implements OnInit{
  myProjects : Project[] = [];
  selectedProject : Project = undefined;

  constructor(private projectService:ProjectService){}


  ngOnInit(): void {
      this.getMyProjects();
  }

  getMyProjects(){
    this.projectService.getAllMyProjects().subscribe({
      next : (projects) => {
        for(let p of projects){
          let imageUrl='http://localhost:8080'+p.imageUrl;
          p.imageUrl=imageUrl;
          this.myProjects.push(p);
        }

      },
      error : (erreur) => {
        console.log("Erreur lors de la récupération de mes  projets. ERREUR : ");
        console.log(erreur);
      }
    })
  }



  selectProject(project:Project){
    this.selectedProject=project;
  }


}
