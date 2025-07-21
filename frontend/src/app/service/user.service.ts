import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/api/auth';


  constructor(private http:HttpClient){}





  /**
   * >  C'est une méthode qui effectue une requête HTTP GET pour
   * récupérer le profil d'un utilisateur depuis une API
   *  > Elle envoie un header d'autorisation avec un token JWT
   * (JSON Web Token) qui est récupéré depuis le localstorage
   * @returns  Observable<any>n
   */
  getProfile() : Observable<any>{
    return this.http.get(`${this.baseUrl}/profile`, {
      headers : {
        'Authorization' : `Bearer ${localStorage.getItem('token')}`
      }
    });
  }
}
