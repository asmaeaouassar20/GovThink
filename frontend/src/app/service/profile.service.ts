import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
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






  // Supprimer la photo de profil
  deleteProfilPicture() : Observable<ApiResponse<UserProfile>>{
    return this.http.delete<ApiResponse<UserProfile>>(`${this.apiUrl}/picture`, {
      headers : this.authService.getAuthHeaders()
    });
  }




  // mettre à jour la photo de profil
  uploadProfilePicture(formData: FormData): Observable<ApiResponse<UserProfile>> {
   
    const token = localStorage.getItem('token');
    
    if (!token) {
      alert('Token manquant - Veuillez vous reconnecter');
      window.location.href = '/login';
      return throwError(() => new Error('Token manquant'));
    }
    
    const headers = new HttpHeaders();
    headers.set('Authorization', `Bearer ${token}`);
    
    // créer un nouvel objet HttpHeaders
    const authHeaders = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    
    return this.http.post<ApiResponse<UserProfile>>(`${this.apiUrl}/picture`, formData, { 
      headers: authHeaders 
    });
  }


  

}
