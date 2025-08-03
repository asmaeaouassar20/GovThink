import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse, ChangePassword, UserProfile } from '../interfaces/profile';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = 'http://localhost:8080/api/profile';

  constructor(private http:HttpClient){}

  
  // Récupérer le profil de l'utilisateur connecté
  getCurrentProfile() : Observable< ApiResponse<UserProfile> > {
    return this.http.get<ApiResponse<UserProfile>>(this.apiUrl);
  }



  // Met à jour le profil de l'utilisateur
  updateProfile(userProfile : UserProfile) : Observable< ApiResponse<UserProfile> > {
    return this.http.put<ApiResponse<UserProfile>>(this.apiUrl,userProfile);
  }


  // Changer le mot de passe
  changePassword(changePassword : ChangePassword) : Observable<ApiResponse<void>>{
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/change-password`,changePassword);   
  }

}
