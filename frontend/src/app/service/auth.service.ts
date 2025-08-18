import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/api/auth';


  constructor(private http: HttpClient, private  router:Router){}



  signup(formData : FormData) : Observable<any>{
    return this.http.post(`${this.baseUrl}/signup`,formData);
  }


  login(credentials : any) : Observable<any>{
    return this.http.post(`${this.baseUrl}/login`,credentials);
  }






  getToken() : string | null {
    return localStorage.getItem('token');
  }
  saveToken(token : string) : void{
    localStorage.setItem('token',token);
  }



  isLoggedIn() : boolean {
    return !!this.getToken(); //pour convertir une valeur en boolean
  }





  // récupérer les headers d'authentification
  getAuthHeaders() : HttpHeaders{
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type' : 'application/json',
      'Authorization' : token ? `Bearer ${token}` :''
    });
  }

  getHeaders() : HttpHeaders {
    const token=localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization' : `Bearer ${token}`
    })
  }




  logout(){
    localStorage.clear();
    this.router.navigate(['/home'])
  }

  
}
