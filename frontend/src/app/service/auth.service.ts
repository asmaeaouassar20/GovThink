import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/api/auth';


  constructor(private http: HttpClient){}



  signup(userData : any) : Observable<any>{
    return this.http.post(`${this.baseUrl}/signup`,userData);
  }
  
}
