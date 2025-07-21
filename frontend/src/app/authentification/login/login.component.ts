import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [NgIf,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  credentials = {email:'' , password:''};
  error = '';

  constructor(private authService : AuthService,
          private router : Router
  ){}


  onLogin() : void {
    this.authService.login(this.credentials).subscribe({

        next : (response) => {
          this.authService.saveToken(response.token);
          this.router.navigate( ['/accueil'])
        },
        error : (error) =>{
          this.error = 'Identifiants invalides';
        }
  

    })
  }

}
