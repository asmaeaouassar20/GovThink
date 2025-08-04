import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse, ChangePassword, UpdateProfile, UserProfile } from '../interfaces/profile';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = 'http://localhost:8080/api/profile';

  constructor(private http:HttpClient, private authService : AuthService){}

  
  // Récupérer le profil de l'utilisateur connecté
  getCurrentProfile() : Observable< ApiResponse<UserProfile> > {
    return this.http.get<ApiResponse<UserProfile>>(this.apiUrl,{
      headers : this.authService.getAuthHeaders()
    });
  }



  // Met à jour le profil de l'utilisateur
  updateProfile(userProfile : UpdateProfile) : Observable< ApiResponse<UserProfile> > {
    return this.http.put<ApiResponse<UserProfile>>(this.apiUrl,userProfile,{
      headers : this.authService.getAuthHeaders()
    });
  }


  // Changer le mot de passe
  changePassword(changePassword : ChangePassword) : Observable<ApiResponse<void>>{
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/change-password`,changePassword);   
  }


  

}
