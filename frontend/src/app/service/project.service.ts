import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Project } from '../interfaces/projects';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private apiUrl = 'http://localhost:8080/api/projects';

  constructor(private http:HttpClient){}

  getAllProjects() : Observable<Project[]>{
    return this.http.get<Project[]>(this.apiUrl);
  }

  publishProject(formData : FormData) : Observable<Project>{
    return this.http.post<Project>(`${this.apiUrl}/upload`,formData);
  }
  
}
